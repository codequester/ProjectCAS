package org.dhravid.cas.resource.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.dhravid.cas.util.CasUtil;

import javax.tools.JavaFileObject;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(CasUtil.PROTECTION_CODE_PKG + ".EnableProtection")
public class EnableProtectionProcessor extends AbstractProcessor 
{

	private Messager messager;	
	private Filer filer;
	private String resourceId;
	
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) 
	{
		super.init(processingEnv);
		messager = processingEnv.getMessager();
		filer = processingEnv.getFiler();
	}	
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) 
	{
		messager.printMessage(Kind.NOTE,"Enabling Protection for the Resources");
		for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(EnableProtection.class)) 
		{
		      if (annotatedElement.getKind() != ElementKind.CLASS) 
		      {
		    	  messager.printMessage(Kind.ERROR ,"Only classes can be annotated with EnableProtection");
		    	  return true; 
		      }
		      EnableProtection annotationClass = annotatedElement.getAnnotation(EnableProtection.class);
		      resourceId = annotationClass.value();
		      injectConfigSource(resourceId);    
		}
		return true;
	}
		
	private void injectConfigSource(String clientId)
	{
        try 
        {
            JavaFileObject javaFileObject = filer.createSourceFile(CasUtil.PROTECTION_CODE_PKG+"."+ CasUtil.RESOURCE_CONFIG_CLASS_NAME);
            messager.printMessage(Kind.NOTE,"Creating " + javaFileObject.toUri());
            Writer javaWriter = javaFileObject.openWriter();
            try 
            {
                PrintWriter printWriter = new PrintWriter(javaWriter);
                printWriter.println("package " + CasUtil.PROTECTION_CODE_PKG + ";");
                injectImports(printWriter);
                injectAnnotations(printWriter);
                injectSource(printWriter);
            } 
            finally 
            {
            	javaWriter.close();
            }
        } 
        catch (IOException ioException) 
        {
            messager.printMessage(Kind.ERROR,ioException.toString());
        }		
	}
	
	private void injectImports(PrintWriter printWriter)
	{
        printWriter.println("import org.springframework.beans.factory.annotation.Autowired;");
        printWriter.println("import org.springframework.context.annotation.ComponentScan;");
        printWriter.println("import org.springframework.context.annotation.Configuration;");
		printWriter.println("import org.springframework.security.config.annotation.web.builders.HttpSecurity;");
		printWriter.println("import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;");
		printWriter.println("import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;");
		printWriter.println("import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;");
	}
	
	private void injectAnnotations(PrintWriter printWriter)
	{
        printWriter.println("@Configuration");
        printWriter.println("@ComponentScan(basePackages={\"org.dhravid.*\"})");
	}
	
	private void injectSource(PrintWriter printWriter)
	{
        printWriter.println("public class "+CasUtil.RESOURCE_CONFIG_CLASS_NAME+" extends AbstractResourceConfig"); 
        printWriter.println("{");
        printWriter.println("	@Autowired");
        printWriter.println("	private ResourceServerTokenServices casTokenService;");

        printWriter.println("   @Override");
        printWriter.println("   public void configure(ResourceServerSecurityConfigurer resources) throws Exception"); 
        printWriter.println("   {");
        printWriter.println("   	resources.resourceId(\""+resourceId+"\");");
        printWriter.println("   	resources.tokenServices(casTokenService);");
        printWriter.println("   }");
    		
        printWriter.println("   @Override");
        printWriter.println("   public void configure(HttpSecurity http) throws Exception"); 
        printWriter.println("   {");
        printWriter.println("   	http.authorizeRequests().anyRequest().authenticated();");
        printWriter.println("   }");
        printWriter.println("}");
	}	
}
