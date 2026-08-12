package com.example.bookprogram1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookprogram1.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
    
    @Select("<script>" +
            "SELECT br.*, r.name as reader_name, b.name as book_name, b.author " +
            "FROM borrow_record br " +
            "LEFT JOIN reader r ON br.reader_id = r.id " +
            "LEFT JOIN book b ON br.book_id = b.id " +
            "<where>" +
            "<if test='status != null and status != \"\"'> AND br.status = #{status}</if>" +
            "</where>" +
            "ORDER BY br.create_time DESC" +
            "</script>")
    IPage<BorrowRecord> selectPageWithDetail(Page<BorrowRecord> page, @Param("status") String status);
    
    @Select("<script>" +
            "SELECT br.*, r.name as reader_name, b.name as book_name, b.author " +
            "FROM borrow_record br " +
            "LEFT JOIN reader r ON br.reader_id = r.id " +
            "LEFT JOIN book b ON br.book_id = b.id " +
            "WHERE br.reader_id = #{readerId} " +
            "<if test='status != null and status != \"\"'> AND br.status = #{status}</if>" +
            "ORDER BY br.create_time DESC" +
            "</script>")
    IPage<BorrowRecord> selectMyPageWithDetail(Page<BorrowRecord> page, @Param("readerId") Long readerId, @Param("status") String status);

    @Select("<script>" +
            "SELECT b.id, b.name as bookName, b.author, b.isbn, COUNT(br.id) as borrowCount " +
            "FROM book b " +
            "LEFT JOIN borrow_record br ON b.id = br.book_id " +
            "GROUP BY b.id, b.name, b.author, b.isbn " +
            "ORDER BY borrowCount DESC " +
            "LIMIT #{limit} " +
            "</script>")
    List<Map<String, Object>> selectTopBooks(@Param("limit") Integer limit);
}