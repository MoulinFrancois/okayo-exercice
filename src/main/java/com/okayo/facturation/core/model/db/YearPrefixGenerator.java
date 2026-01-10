package com.okayo.facturation.core.model.db;

import java.time.LocalDate;
import java.util.Properties;
import org.hibernate.type.BasicType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.spi.TypeConfiguration;

public class YearPrefixGenerator extends SequenceStyleGenerator {

	 @Override
	    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
	        // FORCE Hibernate à utiliser Long pour la lecture de la séquence, même si le champ final est une String.
	        BasicType<Long> longType = new TypeConfiguration().getBasicTypeRegistry().getRegisteredType(Long.class);
	        super.configure(longType, params, serviceRegistry);
	    }

	    @Override
	    public Object generate(SharedSessionContractImplementor session, Object object) {
	        Object sequenceValue = super.generate(session, object);
	        //"2026-XXX"
	        return String.format("%d-%03d", LocalDate.now().getYear(), ((Number) sequenceValue).longValue()
	        );
	    }
	    
}
