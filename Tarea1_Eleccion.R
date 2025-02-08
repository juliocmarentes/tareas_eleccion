

library(tidyverse)
library(ordinal)
library(data.table)
library(parallel)
library(patchwork)

cat_consumidores <- as.data.frame(matrix(1:3,nrow = 3, ncol = 1)) %>%
                    setNames("tipo") %>%
                    mutate(cantidad = 50,
                           ingreso = c(500,4000,10000))

cat_alternativas <- as.data.frame(matrix(1:4,nrow = 4, ncol = 1)) %>%
                    setNames("j") %>%
                    mutate(precio = j * 100)

J <- nrow(cat_alternativas)

p_jn <- crossing(cat_consumidores,  cat_alternativas) %>%
        mutate(v_jn = 3 + 2 * log(ingreso-precio)) %>%
        group_by(tipo) %>%
        mutate(num = exp(v_jn),
               den = sum(num),
               p_jn = num/den) %>%
        ungroup()

episodios <- 10000

total_gumbels <- cat_consumidores %>% 
  summarise(total = sum(cantidad)) %>% pull() *
  episodios * nrow(cat_alternativas)

total_comparaciones <- cat_consumidores %>% 
  summarise(total = sum(cantidad)) %>% pull() *
  episodios

lista <- list()

for(j in 1:episodios){
  listita <- list()
  listita[[1]] <- j
  listita[[2]] <- p_jn
  lista[[j]] <- listita
}

ncores <- detectCores(logical = FALSE)
cl   <- makeCluster(ncores-2, type="PSOCK")
clusterExport(cl=cl,list())
clusterCall(cl, function() library(data.table))

inicio <- Sys.time()

base <- parLapply(cl, lista, function(datos_lista){
  
  library(tidyverse)
  library(ordinal)
  
  #datos_lista <- lista[[1]]
  e_act <- datos_lista[[1]]
  p_jn_act <- datos_lista[[2]]
  tipos <- p_jn_act %>%
           distinct(tipo, .keep_all = FALSE) %>%
           pull()
  
  J <- max(p_jn_act$j)
  
  set.seed(e_act)
  
  base_act <- tibble()
  
  for(tipoo in tipos){
    #tipoo <- tipos[1]
    param_consumidor <- p_jn_act %>%
                  dplyr::filter(tipo == tipoo)
    
    n_t <- param_consumidor$cantidad[1] 
    J_t  <- nrow(param_consumidor)
    
    
    basesita <- as.data.frame(matrix(
      rgumbel(n_t * J_t), ncol = 1 )) %>%
      setNames("e_jn") %>%
      mutate(tipo = tipoo,
             j = rep(1:J, n_t),
             aux = 1:n(),
             consumidor = ceiling(aux/J)) %>%
      left_join(param_consumidor %>%
                dplyr::select(j, v_jn),
                by = "j") %>%
      dplyr::select(-aux) %>%
      mutate(u_jn = v_jn + e_jn) %>%
      group_by(consumidor) %>%
      mutate(sel = ifelse(u_jn == max(u_jn),1,0)) %>%
      ungroup() %>%
      mutate(j2 = paste0("Alt_",j)) %>%
      dplyr::select(tipo, j2, consumidor, sel) %>%
      spread(j2,sel)
      
  base_act <- base_act %>%
              bind_rows(basesita)
    
  }
  
  base_act <- base_act %>%
              mutate(episodio =e_act)
  
  return(base_act)
  
})

base_df <- do.call("rbind",base)

stopCluster(cl)

final <- Sys.time()
final-inicio ## tiempo promedio de 1 minuto

base_long <- base_df %>%
            gather("alternativa","valor",-episodio, -tipo, -consumidor)


demandas <- base_long %>%
            group_by(episodio,alternativa) %>%
            summarise(total = sum(valor)) %>%
            ungroup()

resumen_demandas <- demandas %>%
                    group_by(alternativa) %>%
                    summarise(total_mean = mean(total),
                              total_sd = sd(total))


alternativas <- demandas%>% 
                distinct(alternativa, .keep_all = F) %>%
                pull()


lista_graficos <- list()
for(i in 1:length(alternativas)){
  
  alternativaa <- alternativas[i]
  demanda_act <- demandas %>% 
               dplyr::filter(alternativa == alternativaa) 
  
  plot_act <- ggplot(demanda_act, aes(x = total)) +
                geom_histogram(bins = 35, color="white",
                               fill ="blue") +
                geom_vline(xintercept = resumen_demandas %>%
                  dplyr::filter(alternativa == alternativaa) %>%
                         pull(total_mean),
                  color = "red")+
              theme_minimal()+
              labs(x = "Cantidad demandada",
                   y = "",
                   title = alternativaa)
  
  lista_graficos[[i]] <- plot_act
}  


patchwork::wrap_plots(lista_graficos)  
  

###########################
#   MULTINOMIAL VERSION   #
###########################

ncores <- detectCores(logical = FALSE)
cl   <- makeCluster(ncores-2, type="PSOCK")
clusterExport(cl=cl,list())
clusterCall(cl, function() library(data.table))


inicio <- Sys.time()

base2 <- parLapply(cl, lista, function(datos_lista){
  
  library(tidyverse)
  library(ordinal)
  
  #datos_lista <- lista[[1]]
  e_act <- datos_lista[[1]]
  p_jn_act <- datos_lista[[2]]
  tipos <- p_jn_act %>%
    distinct(tipo, .keep_all = FALSE) %>%
    pull()
  
  J <- max(p_jn_act$j)
  
  set.seed(e_act)
  
  base_act <- tibble()
  
  for(tipoo in tipos){
    #tipoo <- tipos[1]
    param_consumidor <- p_jn_act %>%
      dplyr::filter(tipo == tipoo)
    
    p_act <- param_consumidor %>%
             pull(p_jn)
    
    n_t <- param_consumidor$cantidad[1] 
    J_t  <- nrow(param_consumidor)
    
    basesita <- as.data.frame(t(rmultinom(n_t, 1, p_act))) %>%
                setNames(paste0("Alt_",1:J_t)) %>%
                mutate(consumidor = 1:n_t,
                       tipo = tipoo) %>%
                relocate(consumidor) %>%
                relocate(tipo)
    
    base_act <- base_act %>%
      bind_rows(basesita)
    
  }
  
  base_act <- base_act %>%
    mutate(episodio = e_act)
  
  return(base_act)
  
})

base2_df <- do.call("rbind",base2)

stopCluster(cl)

final <- Sys.time()
final-inicio ## tiempo promedio de 1 minuto

base2_long <- base2_df %>%
  gather("alternativa","valor",-episodio, -tipo, -consumidor)


demandas2 <- base2_long %>%
  group_by(episodio,alternativa) %>%
  summarise(total = sum(valor)) %>%
  ungroup()

resumen_demandas2 <- demandas2 %>%
  group_by(alternativa) %>%
  summarise(total_mean = mean(total),
            total_sd = sd(total))


alternativas2 <- demandas2 %>% 
  distinct(alternativa, .keep_all = F) %>%
  pull()


lista_graficos2 <- list()
for(i in 1:length(alternativas2)){
  
  alternativaa <- alternativas2[i]
  demanda_act <- demandas2 %>% 
    dplyr::filter(alternativa == alternativaa) 
  
  plot_act <- ggplot(demanda_act, aes(x = total)) +
    geom_histogram(bins = 35, color="white",
                   fill ="blue") +
    geom_vline(xintercept = resumen_demandas2 %>%
                 dplyr::filter(alternativa == alternativaa) %>%
                 pull(total_mean),
               color = "red")+
    theme_minimal()+
    labs(x = "Cantidad demandada",
         y = "",
         title = alternativaa)
  
  lista_graficos2[[i]] <- plot_act
}  


patchwork::wrap_plots(lista_graficos2)  

