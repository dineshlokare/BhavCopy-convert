package com.example.BhavCopy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bhavcopy_data") // Map to the corresponding table in PostgreSQL
public class BhavcopyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for primary key
    private Long id; // Primary key for each record

    // Fields mapped to the table columns

    @Column(name = "series")
    public String series; // Maps to the 'series' column

    @Column(name = "open")
    public Double open; // Maps to the 'open' column

    @Column(name = "symbol")
    public String symbol; // Maps to the 'symbol' column

    @Column(name = "high")
    public Double high; // Maps to the 'high' column

    @Column(name = "low")
    public Double low; // Maps to the 'low' column

    @Column(name = "close")
    public Double close; // Maps to the 'close' column

    @Column(name = "last")
    public Double last; // Maps to the 'last' column

    @Column(name = "prev_close")
    public Double prevClose; // Maps to the 'prev_close' column

    @Column(name = "tot_trd_qty")
    public Long totTrdQty; // Maps to the 'tottrd_qty' column

    @Column(name = "tot_trd_val")
    public Double totTrdVal;

    @Column(name = "timestamp")
    public String timestamp; // Maps to the 'timestamp' column

    @Column(name = "total_trades")
    public Integer totalTrades; // Maps to the 'total_trades' column

    @Column(name = "isin")
    public String isin; // Maps to the 'isin' column

    // Getters and setters for each field

    public BhavcopyData(String symbol, String series, Double open, Double high, Double low, Double close, Double last,
            Double prevClose, Long totTrdQty, Double totTrdVal, String timestamp, Integer totalTrades, String isin) {
        this.symbol = symbol;
        this.series = series;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.last = last;
        this.prevClose = prevClose;
        this.totTrdQty = totTrdQty;
        this.totTrdVal = totTrdVal;
        this.timestamp = timestamp;
        this.totalTrades = totalTrades;
        this.isin = isin;
    }

    BhavcopyData() {
    }

}
