package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JPADatabaseManager extends AbstractDatabaseManager {

    private static final JPADatabaseManager.JPADatabaseManagerFactory FACTORY = new JPADatabaseManager.JPADatabaseManagerFactory((JPADatabaseManager.SyntheticClass_1) null);
    private final String entityClassName;
    private final Constructor entityConstructor;
    private final String persistenceUnitName;
    private EntityManagerFactory entityManagerFactory;

    private JPADatabaseManager(String s, int i, Class oclass, Constructor constructor, String s1) {
        super(s, i);
        this.entityClassName = oclass.getName();
        this.entityConstructor = constructor;
        this.persistenceUnitName = s1;
    }

    protected void connectInternal() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
    }

    protected void disconnectInternal() {
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }

    }

    protected void writeInternal(LogEvent logevent) {
        if (this.isConnected() && this.entityManagerFactory != null) {
            AbstractLogEventWrapperEntity abstractlogeventwrapperentity;

            try {
                abstractlogeventwrapperentity = (AbstractLogEventWrapperEntity) this.entityConstructor.newInstance(new Object[] { logevent});
            } catch (Exception exception) {
                throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", exception);
            }

            EntityManager entitymanager = null;
            EntityTransaction entitytransaction = null;

            try {
                entitymanager = this.entityManagerFactory.createEntityManager();
                entitytransaction = entitymanager.getTransaction();
                entitytransaction.begin();
                entitymanager.persist(abstractlogeventwrapperentity);
                entitytransaction.commit();
            } catch (Exception exception1) {
                if (entitytransaction != null && entitytransaction.isActive()) {
                    entitytransaction.rollback();
                }

                throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + exception1.getMessage(), exception1);
            } finally {
                if (entitymanager != null && entitymanager.isOpen()) {
                    entitymanager.close();
                }

            }

        } else {
            throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
        }
    }

    public static JPADatabaseManager getJPADatabaseManager(String s, int i, Class oclass, Constructor constructor, String s1) {
        return (JPADatabaseManager) AbstractDatabaseManager.getManager(s, (AbstractDatabaseManager.AbstractFactoryData) (new JPADatabaseManager.FactoryData(i, oclass, constructor, s1)), (ManagerFactory) JPADatabaseManager.FACTORY);
    }

    JPADatabaseManager(String s, int i, Class oclass, Constructor constructor, String s1, JPADatabaseManager.SyntheticClass_1 jpadatabasemanager_syntheticclass_1) {
        this(s, i, oclass, constructor, s1);
    }

    static class SyntheticClass_1 {    }

    private static final class JPADatabaseManagerFactory implements ManagerFactory {

        private JPADatabaseManagerFactory() {}

        public JPADatabaseManager createManager(String s, JPADatabaseManager.FactoryData jpadatabasemanager_factorydata) {
            return new JPADatabaseManager(s, jpadatabasemanager_factorydata.getBufferSize(), jpadatabasemanager_factorydata.entityClass, jpadatabasemanager_factorydata.entityConstructor, jpadatabasemanager_factorydata.persistenceUnitName, (JPADatabaseManager.SyntheticClass_1) null);
        }

        JPADatabaseManagerFactory(JPADatabaseManager.SyntheticClass_1 jpadatabasemanager_syntheticclass_1) {
            this();
        }
    }

    private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {

        private final Class entityClass;
        private final Constructor entityConstructor;
        private final String persistenceUnitName;

        protected FactoryData(int i, Class oclass, Constructor constructor, String s) {
            super(i);
            this.entityClass = oclass;
            this.entityConstructor = constructor;
            this.persistenceUnitName = s;
        }
    }
}
