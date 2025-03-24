

library(tidyverse)
library(patchwork)

ruta_ant <- "C:/Users/Mi/Documents/Maestrias/Colmex/4to semestre/eleccion/tareas eleccion/Tarea2/data"
base_ant.obs <- read.csv(file.path(ruta_ant, "resultado.csv"),
                     header = F)

base_ant <- base_ant.obs %>%
  setNames(paste0("logit_",c("alpha_1",
                    "alpha_2",
                    "alpha_3",
                    "beta_price",
                    "beta_feat"))) %>%
  relocate(logit_beta_price, .after = logit_beta_feat)

resumen_base_ant <- base_ant %>%
  gather("serie","valor") %>%
  group_by(serie) %>%
  summarise(mean = mean(valor)) %>%
  ungroup()

ruta <- "C:/Users/Mi/Documents/Maestrias/Colmex/4to semestre/eleccion/tareas eleccion/Tarea3/data"
base.obs <- read.csv(file.path(ruta, "resultado.csv"),
                     header = F)

base <- base.obs %>%
        setNames(paste0("nlogit_",c("alpha_1",
                          "alpha_2",
                          "alpha_3",
                          "beta_price",
                          "beta_feat",
                          "gamma_1",
                          "gamma_2"))) %>%
         relocate(nlogit_beta_price, .after = nlogit_beta_feat) %>%
         mutate(nlogit_lambda_1 = exp(nlogit_gamma_1)/(1+exp(nlogit_gamma_1)),
                nlogit_lambda_2 = exp(nlogit_gamma_2)/(1+exp(nlogit_gamma_2))) %>%
         select(-nlogit_gamma_1, -nlogit_gamma_2)

nombres <- data.frame(nombre= (names(base))) %>%
           mutate(nombre2 = substr(nombre, 8, nchar(nombre)))  

base2 <- base %>%
         setNames(nombres%>%pull(nombre2))

resumen_base <- base %>%
                gather("serie","valor") %>%
                group_by(serie) %>%
                summarise(mean = mean(valor)) %>%
                ungroup()

Sigma <- cov(base2)

lista_graficos <- list()

for(i in 1:ncol(base)){
  #i <- 1
  datos_i <- base %>%
             select(i) %>%
             setNames(c("dato"))
  
  plot_act <- ggplot(datos_i, aes(x = dato)) +
    geom_histogram(bins = 35, color="white",
                   fill ="blue") +
    geom_vline(xintercept = resumen_base$mean[i],
               color = "red")+
    theme_minimal()+
    labs(x = "",
         y = "",
         title = resumen_base$serie[i])
  plot_act
  lista_graficos[[i]] <- plot_act
}  

plot1 <- patchwork::wrap_plots(lista_graficos)  
plot1



        