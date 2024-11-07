package com.example.BhavCopy.repository;

import com.example.BhavCopy.entity.BhavcopyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BhavcopyDataRepository extends JpaRepository<BhavcopyData, Long> {

    // Custom query to find Bhavcopy data by symbol
    @Query(value = "SELECT * FROM get_bhavcopy_data_by_symbol(:symbol)", nativeQuery = true)
    List<BhavcopyData> findBySymbolUsingProcedure(@Param("symbol") String symbol);

    @Query(value = "SELECT count_symbols_by_series(:series)", nativeQuery = true)
    Long countBySeries(@Param("series") String series);

    @Query(value = "SELECT * FROM get_symbols_with_gain_greater_than(:n)", nativeQuery = true)
    List<String> findSymbolsWithGainGreaterThan(@Param("n") Double n);

    @Query(value = "SELECT symbol FROM get_symbols_with_high_low_greater_than(:n)", nativeQuery = true)
    List<String> findSymbolsWithHighLowGreaterThan(@Param("n") Double n);

    @Query(value = "SELECT get_standard_deviation_by_series(:series)", nativeQuery = true)
    Double calculateStandardDeviation(@Param("series") String series);

    @Query(value = "SELECT * FROM get_top_n_gain_symbols(:n)", nativeQuery = true)
    List<String> findTopNGainSymbols(@Param("n") int n);

    @Query(value = "SELECT * FROM get_bottom_n_laggards(:n)", nativeQuery = true)
    List<String> findBottomNLaggards(@Param("n") int n);

    @Query(value = "SELECT * FROM get_top_n_traded_symbols(:n)", nativeQuery = true)
    List<String> findTopNTradedSymbols(@Param("n") int n);

    @Query(value = "SELECT * FROM get_bottom_n_traded_symbols(:n)", nativeQuery = true)
    List<String> findBottomNTradedSymbols(@Param("n") int n);

    @Query(value = "SELECT * FROM get_high_low_traded_symbols(:series)", nativeQuery = true)
    List<Object[]> findHighLowTradedSymbols(@Param("series") String series);
}
