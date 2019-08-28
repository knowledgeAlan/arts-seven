package com.zzm;

import com.zzm.dao.BlogMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class MbatisSpringTest
{
    public static void main( String[] args )
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        BlogMapper blogMapper = ctx.getBean(BlogMapper.class);
        blogMapper.selectBlog(1);
    }
}
