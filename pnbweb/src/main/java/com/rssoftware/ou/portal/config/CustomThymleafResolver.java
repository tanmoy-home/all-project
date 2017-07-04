package com.rssoftware.ou.portal.config;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.util.ClassLoaderUtils;
import org.thymeleaf.util.Validate;

public class CustomThymleafResolver implements IResourceResolver {

	private static final String CSRF_INPUT_ELEMENT = "<input type=\"hidden\" th:value=\"${_csrf.token}\" th:name=\"${_csrf.parameterName}\" />";
	private static final String FORM_TAG_METHOD_ATTR_NAME = "method";
	private static final String FORM_TAG_NAME = "form";

	public CustomThymleafResolver() {
		super();
	}

	public String getName() {
		return getClass().getName().toUpperCase();
	}

	public InputStream getResourceAsStream(final TemplateProcessingParameters params, final String resourceName) {
		Validate.notNull(resourceName, "Resource name cannot be null");
		if (StringContext.class.isAssignableFrom(params.getContext().getClass())) {
			String content = ((StringContext) params.getContext()).getContent();
			return IOUtils.toInputStream(insertCsrfParamsToForm(content));
		}
		return ClassLoaderUtils.getClassLoader(ClassLoaderResourceResolver.class).getResourceAsStream(resourceName);
	}

	public static class StringContext extends WebContext {

		private String content;

		public StringContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext,
				String content) {
			super(request, response, servletContext);
			this.content = content;
		}

		public String getContent() {
			return content;
		}
	}

	private String insertCsrfParamsToForm(String content) throws TransformerFactoryConfigurationError {
		Document html = Jsoup.parse(content);
		Elements forms = html.getElementsByTag(FORM_TAG_NAME);
		for (Element form : forms) {
			String formMethod = form.attr(FORM_TAG_METHOD_ATTR_NAME);
			if (StringUtils.isNotBlank(formMethod) && "POST".equalsIgnoreCase(formMethod)) {
				form.append(CSRF_INPUT_ELEMENT);
			}
		}
		return html.body().html();
	}

}