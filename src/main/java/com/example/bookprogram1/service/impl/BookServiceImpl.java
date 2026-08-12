package com.example.bookprogram1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookprogram1.entity.Book;
import com.example.bookprogram1.entity.BookCategory;
import com.example.bookprogram1.mapper.BookMapper;
import com.example.bookprogram1.service.BookCategoryService;
import com.example.bookprogram1.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final BookCategoryService bookCategoryService;

    /**
     * 获取图书列表，包含分类名称
     */
    @Override
    public List<Book> getListWithCategoryName() {
        List<Book> books = this.list();
        if (books.isEmpty()) {
            return books;
        }

        // 获取所有分类ID
        List<Long> categoryIds = books.stream()
                .map(Book::getCategoryId)
                .distinct()
                .filter(id -> id != null)
                .collect(Collectors.toList());

        if (!categoryIds.isEmpty()) {
            // 查询分类信息
            Map<Long, BookCategory> categoryMap = bookCategoryService.listByIds(categoryIds)
                    .stream()
                    .collect(Collectors.toMap(BookCategory::getId, category -> category));

            // 设置分类名称
            books.forEach(book -> {
                if (book.getCategoryId() != null) {
                    BookCategory category = categoryMap.get(book.getCategoryId());
                    if (category != null) {
                        book.setCategoryName(category.getName());
                    }
                }
            });
        }

        return books;
    }

    /**
     * 根据分类ID获取图书列表
     */
    @Override
    public List<Book> getListByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getCategoryId, categoryId);
        List<Book> books = this.list(wrapper);

        if (!books.isEmpty() && categoryId != null) {
            BookCategory category = bookCategoryService.getById(categoryId);
            if (category != null) {
                books.forEach(book -> book.setCategoryName(category.getName()));
            }
        }

        return books;
    }
}