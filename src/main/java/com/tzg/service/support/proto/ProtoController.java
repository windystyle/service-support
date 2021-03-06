package com.tzg.service.support.proto;

import com.tzg.service.support.json.JsonResp;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象控制器原型类。并提供基本的http请求方法支持。比如get, post, delete, index, put等。
 *
 * @author 曾林 2016/12/7.
 */
public abstract class ProtoController< T extends ProtoBean > extends ProtoBaseController {

    protected abstract ProtoService getService();

    /* =============================================================== */
    /* http方法调用支持。包括post、delete、put、get和index等方法           */
    /* =============================================================== */
    @SuppressWarnings( "unchecked" )
    @ResponseBody
    @RequestMapping( "/proto/post" )
    public JsonResp post( T bean ) throws Exception {
        getService().insert( bean );
        return getSuccessJsonResp( bean );
    }

    @ResponseBody
    @RequestMapping( "/proto/delete" )
    public JsonResp delete( Integer id ) throws Exception {
        getService().delete( id );
        return getSuccessJsonResp();
    }

    @SuppressWarnings( "unchecked" )
    @ResponseBody
    @RequestMapping( "/proto/deleteList" )
    public JsonResp deleteList( String ids ) throws Exception {

        Map< String, Object > map = new HashMap<>();
        map.put( "ids", Arrays.asList( ids.split( "-" ) ) );

        getService().deleteList( map );
        return getSuccessJsonResp();

    }

    @SuppressWarnings( "unchecked" )
    @ResponseBody
    @RequestMapping( "/proto/put" )
    public JsonResp put( T bean ) throws Exception {
        getService().update( bean );
        return getSuccessJsonResp();
    }

    @SuppressWarnings( "unchecked" )
    @ResponseBody
    @RequestMapping( "/proto/get" )
    public JsonResp get( Integer id ) throws Exception {
        T t = ( T ) getService().selectById( id );
        return getSuccessJsonResp( t, 1 );
    }

    @SuppressWarnings( "unchecked" )
    @ResponseBody
    @RequestMapping( "/proto/index" )
    public JsonResp index( T bean, @RequestParam( value = "pageIndex", required = false ) Integer pageIndex, @RequestParam( value = "pageSize", required = false ) Integer pageSize )
            throws Exception {

        Map< String, Object > map = getPagingQryMap( bean, pageIndex, pageSize );

        Integer   count = getService().selectCount( map );
        List< T > list  = getService().selectList( map );

        return getSuccessJsonResp( list, count );

    }

}
