package com.dh.series.service;

import java.util.List;
import java.util.Objects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dh.series.model.Series;
import com.dh.series.repository.SeriesRepository;

@Service
public class SeriesService {
    //agrego loggeo
    private final Logger LOG = LoggerFactory.getLogger(SeriesService.class);
    @Value("$queue.serie") //trae el nombre de la cola del booststrap
    private String serieQueue;

    private final RabbitTemplate rabbitTemplate;
    private final SeriesRepository seriesRepository;

    @Autowired
    public SeriesService(RabbitTemplate rabbitTemplate, SeriesRepository seriesRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.seriesRepository = seriesRepository;
    }

    public Series findById(String id) {
        return seriesRepository.findById(id)
            .orElse(null);
    }

    public List<Series> findAll() {
        LOG.info("se va a buscar lista de series");
        return seriesRepository.findAll();
    }
    public List<Series> findByGenre(String genre) {

        List<Series> series = seriesRepository.findByGenre(genre);

        return series;
    }


    public Series saveSeries(Series series) {
        return seriesRepository.save(series);
    }

    //creo metodo para guardar en cola
    public void saveSeriesRabbit(Series serie){
        rabbitTemplate.convertAndSend(serieQueue, serie);
    }

}
