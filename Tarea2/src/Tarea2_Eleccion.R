

library(tidyverse)
library(patchwork)

ruta <- "C:/Users/Mi/Documents/Maestrías/Colmex/4to semestre/eleccion/tareas eleccion/Tarea2/data"
base.obs <- read.csv(file.path(ruta, "resultado.csv"),
                     header = F)

base <- base.obs %>%
        setNames(c("alpha_1",
                   "alpha_2",
                   "alpha_3",
                   "beta_price",
                   "beta_feat")) %>%
         relocate(beta_price, .after = beta_feat)

resumen_base <- base %>%
                gather("serie","valor") %>%
                group_by(serie) %>%
                summarise(mean = mean(valor)) %>%
                ungroup()

Sigma <- cov(base)

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




        