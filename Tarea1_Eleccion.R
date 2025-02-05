

library(tidyverse)
library(ordinal)

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

base<- tibble()

for(e in 1:episodios){
  print(paste0("Episodio ",e))
  #e <- 1
  for(t in 1:nrow(cat_consumidores)){
    #t <- 1
    p_act <- p_jn %>%
      dplyr::filter(tipo == t) %>%
      pull(p_jn)
    
    epsilons_act <- rmultinom(n = cat_consumidores$cantidad[t] , 1 , p_act)
    
    
    
    for(c in 1:cat_consumidores$cantidad[t]){
     # c <- 1
      id_act <- paste0(e,"_",t,"_",c)
      r <- as.data.frame(matrix(c(id_act,rmultinom(1, 1, p_act)),
                                nrow = 1, ncol = (J+1))) %>%
        setNames(c("id", paste0("Alt_",1:J)))
      
      base <- base %>%
              rbind(r)
    }
  }
}
