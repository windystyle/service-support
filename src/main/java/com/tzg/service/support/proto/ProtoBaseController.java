package com.tzg.service.support.proto;

import com.tzg.service.support.json.JsonResp;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class ProtoBaseController< T extends ProtoBean > {

    /**
     * 将实体对象数据结构转变成map数据结构。为mybatis配置文件做请求参数准备。例如：
     * <p>
     * Person person = new Person();
     * person.setName( "mark" );
     * person.setAge( 36 );
     * <p>
     * 会被转换为：
     * Map<String, Object> map = new HashMap();
     * map.put( "name", "mark" );
     * map.put( "age", 36 );
     *
     * @param bean 实体对象
     * @throws IllegalAccessException 不合法的访问异常
     */
    protected Map< String, Object > getQryMap( T bean ) throws IllegalAccessException {
        Map< String, Object > map = new HashMap<>();

        if ( bean != null ) {
            for ( Field field : bean.getClass().getDeclaredFields() ) {
                field.setAccessible( Boolean.TRUE );
                map.put( field.getName(), field.get( bean ) );
            }
        }

        return map;
    }

    /**
     * 将实体对象数据结构转化为map数据结构，并增加分页参数。为mybatis配置文件做请求参数准备。
     * pageNum不能小于1，如果小于1，则会被设置为1。
     * pageSize不能小于0，如果小于0，则会被设置为0。
     *
     * @param bean      实体对象
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @throws IllegalAccessException 不合法的访问异常
     */
    protected Map< String, Object > getPagingQryMap( T bean, Integer pageIndex, Integer pageSize )
            throws IllegalAccessException {
        Map< String, Object > map = getQryMap( bean );

        if ( pageIndex == null || pageIndex < 1 ) pageIndex = 1;
        if ( pageSize == null || pageSize < 0 ) pageSize = 0;

        map.put( "skip", ( pageIndex - 1 ) * pageSize );
        map.put( "size", pageSize );

        return map;
    }

    /**
     * 返回成功json响应。
     */
    protected JsonResp getSuccessJsonResp() {
        return new JsonResp();
    }

    /**
     * 返回成功json响应。如果count参数不存在，则响应中不包含分页记录总数；如果count参数存在且参数个数为1，则响应中包含分页记录总数。如果count
     * 参数存在且参数个数大于1，则会抛出不合法的参数异常，提示函数使用错误。
     *
     * @param data  响应主体。一般是实体（ bean ）记录或者实体记录列表( bean list )。
     * @param count 可变参数。如果不存在，则表示响应主体中不包含分页记录总数；如果存在，则表示响应主体中包含分页记录总数
     * @throws IllegalArgumentException 参数个数不正确异常
     */
    protected JsonResp getSuccessJsonResp( Object data, Integer... count ) throws IllegalArgumentException {

        JsonResp jsonResp = null;

        if ( count.length == 0 ) jsonResp = new JsonResp( JsonResp.OK, data ); // 不包含分页数据

        if ( count.length == 1 ) jsonResp = new JsonResp( JsonResp.OK, count[ 0 ], data ); // 包含分页数据记录数

        /* 如果参数count存在，且不止1个参数的话，那么将报运行时异常，提示方法使用错误 */
        if ( count.length > 1 ) throw new IllegalArgumentException( "argument 'count' length must be 0 or 1" );

        return jsonResp;

    }

    /**
     * 返回失败json响应。响应主体是异常本地化消息。响应status是"error"字符串。
     *
     * @param err 异常主体
     */
    protected JsonResp getErrorJsonResp( Exception err ) {
        return new JsonResp( JsonResp.ERROR, err.getLocalizedMessage() );
    }

    /**
     * 定位到指定的视图。比如controller指定的requestMapping是/authority/user。如果指定view为main。则
     * 最终定位到的view目录为WEB-INF/views/authority/user/main.jsp。
     */
    @RequestMapping( "/{view}" )
    public String locate( @PathVariable String view ) {

        StringBuilder builder = new StringBuilder();

        for ( Annotation annotation : this.getClass().getAnnotations() ) {
            if ( "RequestMapping".equalsIgnoreCase( annotation.annotationType().getSimpleName() ) ) {
                RequestMapping requestMapping = ( RequestMapping ) annotation;
                builder.append( ( requestMapping.value() )[ 0 ] ).append( "/" ).append( view );
                break;
            }
        }

        return builder.toString();
    }

}
