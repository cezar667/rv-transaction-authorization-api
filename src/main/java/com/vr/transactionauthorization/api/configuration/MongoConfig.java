package com.vr.transactionauthorization.api.configuration;

import com.mongodb.MongoClientSettings;
import com.vr.transactionauthorization.api.model.Card;
import java.util.Collection;
import java.util.Collections;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.MongoDbFactoryParser;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Override
  protected String getDatabaseName() {
    return "";
  }

}

