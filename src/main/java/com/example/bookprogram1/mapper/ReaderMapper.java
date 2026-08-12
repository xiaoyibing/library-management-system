package com.example.bookprogram1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookprogram1.entity.Reader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReaderMapper extends BaseMapper<Reader> {
    
    @Select("SELECT r.*, (SELECT COUNT(*) FROM borrow_record br WHERE br.reader_id = r.id AND br.status = 'BORROWED') as borrow_count FROM reader r WHERE r.user_id = #{userId}")
    Reader findByUserId(Long userId);
}
