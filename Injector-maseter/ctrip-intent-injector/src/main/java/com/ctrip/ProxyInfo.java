package com.ctrip;

import com.ctrip.model.DataInfo;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

/**
 * Created by xgc on 16/7/4.
 */
public class ProxyInfo
{
    private String packageName;
    private String targetClassName;
    private String proxyClassName;
    private TypeElement typeElement;

    private Map<String, DataInfo> idViewMap = new HashMap<String, DataInfo>();

    public static final String PROXY = "CTRIP";

    public ProxyInfo(String packageName, String className)
    {
        this.packageName = packageName;
        this.targetClassName = className;
        this.proxyClassName = className + "$$" + PROXY;
    }

    public void putViewInfo(String id, DataInfo viewInfo)
    {
        idViewMap.put(id, viewInfo);
    }

    public Map<String, DataInfo> getIdViewMap()
    {
        return idViewMap;
    }

    public String getProxyClassFullName()
    {
        return packageName + "." + proxyClassName;
    }

    public String generateJavaCode()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from XgcIntentInjector. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");

        builder.append("import com.ctrip.model.Finder;\n");
        builder.append("import com.ctrip.AbstractInjector;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName);
        builder.append("<T extends ").append(getTargetClassName()).append(">");
        builder.append(" implements AbstractInjector<T>");
        builder.append(" {\n");

        generateInjectMethod(builder);
        builder.append('\n');

        builder.append("}\n");
        return builder.toString();

    }

    private String getTargetClassName()
    {
        return targetClassName.replace("$",".");
    }

    private void generateInjectMethod(StringBuilder builder)
    {

        builder.append("  @Override ")
                .append("public void inject(final Finder finder, final T target, Object source) {\n");

        builder.append("    Object data;\n");

        for (String key : idViewMap.keySet())
        {
            DataInfo viewInfo = idViewMap.get(key);
            builder.append("    data = ");
            builder.append("finder.getIntentSource(source , \"");
            builder.append(viewInfo.getId() + "\" );\n");
            builder.append("    target.").append(viewInfo.getName())
                    .append(" = ");
            builder.append("finder.castView( data );\n");
        }

        builder.append("  }\n");
    }

    public TypeElement getTypeElement()
    {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement)
    {
        this.typeElement = typeElement;
    }

}
