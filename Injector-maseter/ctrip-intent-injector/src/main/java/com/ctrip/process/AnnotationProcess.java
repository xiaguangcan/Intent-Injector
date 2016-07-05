package com.ctrip.process;

import com.ctrip.annotation.Parameter;
import com.ctrip.ProxyInfo;
import com.ctrip.model.DataInfo;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by gcxia on 2016/7/1.
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(value = SourceVersion.RELEASE_6)
public class AnnotationProcess extends AbstractProcessor {

    private Map<String, ProxyInfo> mProxyMap = new HashMap<String, ProxyInfo>();

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        String fqClassName, className, packageName;
        for(Element element : roundEnv.getElementsAnnotatedWith(Parameter.class)){
            if(element.getKind() == ElementKind.FIELD){
                //表示一个字段、enum 常量、方法或构造方法参数、局部变量或异常参数。
                VariableElement varElement = (VariableElement) element;
                TypeElement classElement = (TypeElement) element.getEnclosingElement();


                fqClassName = classElement.getQualifiedName().toString();
                PackageElement packageElement = elementUtils.getPackageOf(classElement);
                log("----------------------------------------------------\n----------------------------------------------");
                packageName = packageElement.getQualifiedName().toString();

                className = getClassName(classElement, packageName);

                String id = varElement.getAnnotation(Parameter.class).value();
                String fieldName = varElement.getSimpleName().toString();
                String fieldType = varElement.asType().toString();
                com.ctrip.ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
                if (proxyInfo == null)
                {
                    proxyInfo = new com.ctrip.ProxyInfo(packageName, className);
                    mProxyMap.put(fqClassName, proxyInfo);
                    proxyInfo.setTypeElement(classElement);
                }
                proxyInfo.putViewInfo(id,
                        new DataInfo(id, fieldName, fieldType));
            }
        }

        for (String key : mProxyMap.keySet())
        {
            com.ctrip.ProxyInfo proxyInfo = mProxyMap.get(key);
            try
            {

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e)
            {
            }

        }
        return true;
    }

    private static String getClassName(TypeElement type, String packageName)
    {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    private void log(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, " FATAL ERROR: " + msg);
    }
}
