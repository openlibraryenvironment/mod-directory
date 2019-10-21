package org.olf.reshare;

import static groovy.json.JsonOutput.*

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

import grails.core.GrailsApplication


public class EventPublicationService {

  private KafkaProducer producer = null;
  GrailsApplication grailsApplication

  @javax.annotation.PostConstruct
  public void init() {
    log.debug("Configuring event publication service")
    Properties props = new Properties()
    try {
      grailsApplication.config.events.publisher.toProperties().each { final String key, final String value ->
        // Directly access each entry to cause lookup from env
        log.debug("Configuring event publication service :: ${key} ${value}");
        String prop = grailsApplication.config.getProperty("events.publisher.${key}")
        props.setProperty(key, prop)
      }

      log.debug("Configure EventPublicationService :: ${props}");
      producer = new KafkaProducer(props)
    }
    catch ( Exception e ) {
      log.debug("EventPublicationService::init() problem configuring producer",e);
    }
  }

  public void publishAsJSON(String topic, String key, Map data) {

    log.debug("publishAsJSON(topic=${topic}, key=${key}, ...)");

    try {
      if ( key == null )
        key = new Random().nextLong()

      String compoundMessage = groovy.json.JsonOutput.toJson(data)

      if ( producer ) {
        // log.debug("publishAsJSON(topic:${topic} key:${key}, compoundMessage: ${compoundMessage})");
        producer.send(
            new ProducerRecord<String, String>(topic, key, compoundMessage), { RecordMetadata metadata, Exception e ->
              // println "The offset of the record we just sent is: ${metadata?.offset()}"
            }
        )
      }
      else {
        log.error("Producer not available");
      }
    }
    catch ( Exception e ) {
      log.error("problem trying to publish event",e);
    }
    finally {
      log.debug("producer.send completed");
    }

  }

  @javax.annotation.PreDestroy
  private void cleanUp() throws Exception {
    log.info("EventPublicationService::cleanUp");
    if ( producer != null ) {
      producer.close();
      producer = null;
    }
  }
}
