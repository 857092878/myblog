# myblog
springboot,thymeleaf,swagger,mybatis-plus,redis,mysql,JavaMailSender



项目演示：
项目简介：该项目用于记录个人博客，笔记，生活照片分享，及资源分享。整个项目分为了前台和后台。

前台： 
首页：最新博客，推荐博客，博客搜索，博客留言及回复功能

分类：博客分类 

流年记：文章展示

音乐盒：个人音乐收藏

留言板：留言及回复功能

友人帐：友链及位置

照片墙：照片展示

资源库：推荐资源及路径

关于我：个人介绍

后台：
文章管理，分类管理，流年记管理，友链管理，相册管理，资源管理

功能简介：
		分页功能，采用pagehelper对文章等列表进行分页\
		借口限流：定义接口采用拦截器查看用户ip，对10秒了连续访问多次的ip进行拦截\
		后台管理：采用mybatis-plus的简易代码对数据进行增删改查\
		代码生成器：采用freemaker对数据中的实例类services层和mapper层进行快速创建\
		缓存：采用StringTemplate创建一个新的service层直接面向数据增删改查进行缓存。\
		消息队列：采用一个临时列表存放消息，根据父级id对应一个消息不断向下查找回复。\
		邮件发送：采用JavaMailSender连接网页邮件对用户的留言回复提醒。\
		文章搜索功能：利用mysql的模糊查询收缩文章。\
		Markdown转html：利用commonmark对数据库的markdowm格式转换成html格式显示。\
		文件上传：利用过滤器将上传的文件源映射到一个虚拟路径返回给前端。\
	涉及技术：springboot，swagger2，thymeleaf，mybatis-plus，redis，mysql，JavaMailSender，pagehelpe，jquery
        

![image](https://github.com/857092878/myblog/blob/master/QQ%E5%9B%BE%E7%89%8720220926121354.png)

![image](https://github.com/857092878/myblog/blob/master/QQ%E5%9B%BE%E7%89%8720220925153040.png)
代码地址：https://github.com/857092878/blog/tree/master

修改一个小bug
管理员在每次修改博客内容是修改前的内容不显示于是
在blog-input.html中的form表单里,所有表单要提交的字段中th:value内容修改为  th:text="${blog.getDescription()}"这种形式的字段

我的csdn：https://blog.csdn.net/weixin_57730366?type=blog
