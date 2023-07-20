package org.ascending.project.repository;

import org.ascending.project.model.Car;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CarHibernateDaoImpl implements ICarDao {
    public static final Logger logger = LoggerFactory.getLogger(CarHibernateDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void save(Car car){
        logger.info("Start to getCar from Postgres via Hibernate");

        Transaction transaction = null;

        try{
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(car);
            transaction.commit();
            session.close();

        } catch (HibernateException e){
            logger.error("Unable to open or close", e);
        }
        logger.info("Hava already save {}", car);
    }

    @Override
    public List<Car> getCars(){

        logger.info("Start to getCar from Postgres via Hibernate");

        List<Car> cars = new ArrayList<>();
        Session session = sessionFactory.openSession();

        try{

            String hql = "from Car";
            Query<Car> query = session.createQuery(hql);

            cars = query.list();
            session.close();

        } catch (HibernateException e){
            logger.error("Unable to open or close", e);
        }

        logger.info("Get cars {}", cars);
        return cars;
    }

    @Override
    // Update
    public Car getById(long id) {
        logger.info("Start to getCar from Postgres via Hibernate");
        Session session = sessionFactory.openSession();
        String hql = "FROM Car ca where id = :Id";

        try{

            Query<Car> query = session.createQuery(hql);
            query.setParameter("Id", id);
            Car result = query.uniqueResult();
            session.close();
            return result;

        } catch (HibernateException e){
            logger.error("Unable to open or close", e);
            session.close();
            return null;
        }
    }

    @Override
    public void delete(Car car) {
        logger.info("Start to getCar from Postgres via Hibernate");

        Transaction transaction = null;

        Session session = sessionFactory.openSession();

        try{

            transaction = session.beginTransaction();
            session.delete(car);
            transaction.commit();
            session.close();

        } catch (HibernateException e){
            if (transaction != null){
                logger.error("Delete transaction failed, Rollback...");
                transaction.rollback();
            }
            logger.error("Unable to open or close", e);
        }
        logger.info("Hava already delete {}", car);
    }

    @Override
    public Car update(Car car){
        Transaction transaction = null;
        Session session = sessionFactory.openSession();

        try{
            transaction = session.beginTransaction();
            session.update(car);
            Car ca = getById(car.getId());
            transaction.commit();
            session.close();
            return ca;
        } catch (HibernateException e){
            if (transaction != null){
                transaction.rollback();
            }
            logger.error("failed to insert record", e);
            return null;
        }
    }

//    @Override
//    public Car getCarEagerBy(Long id){
//        String hql = "FROM Car c LEFT JOIN FETCH c.customers where c.id :Id";
//        Session
//    }
}

