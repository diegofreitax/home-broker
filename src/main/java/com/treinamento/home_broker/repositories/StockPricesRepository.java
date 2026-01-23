    package com.treinamento.home_broker.repositories;

    import com.treinamento.home_broker.entities.StockPrice;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface StockPricesRepository extends JpaRepository<StockPrice, Long> {

    }


