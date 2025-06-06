/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.features.DocumentationFeature;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaJerseyServerCodegen extends AbstractJavaJAXRSServerCodegen {

    protected static final String LIBRARY_JERSEY2 = "jersey2";
    protected static final String LIBRARY_JERSEY3 = "jersey3";

    /**
     * Default library template to use. (Default: jersey2)
     */
    public static final String DEFAULT_JERSEY_LIBRARY = LIBRARY_JERSEY2;

    public JavaJerseyServerCodegen() {
        super();

        modifyFeatureSet(features -> features.includeDocumentationFeatures(DocumentationFeature.Readme));

        outputFolder = "generated-code/JavaJaxRS-Jersey";

        apiTemplateFiles.put("apiService.mustache", ".java");
        apiTemplateFiles.put("apiServiceImpl.mustache", ".java");
        apiTemplateFiles.put("apiServiceFactory.mustache", ".java");
        apiTestTemplateFiles.clear(); // TODO: add test template

        // clear model and api doc template as this codegen
        // does not support auto-generated markdown doc at the moment
        //TODO: add doc templates
        modelDocTemplateFiles.remove("model_doc.mustache");
        apiDocTemplateFiles.remove("api_doc.mustache");

        embeddedTemplateDir = templateDir = JAXRS_TEMPLATE_DIRECTORY_NAME;

        CliOption library = new CliOption(CodegenConstants.LIBRARY, CodegenConstants.LIBRARY_DESC).defaultValue(DEFAULT_JERSEY_LIBRARY);
        supportedLibraries.put(LIBRARY_JERSEY2, "Jersey core 2.x");
        supportedLibraries.put(LIBRARY_JERSEY3, "Jersey core 3.x");
        library.setEnum(supportedLibraries);
        cliOptions.add(library);
    }

    @Override
    public String getName() {
        return "jaxrs-jersey";
    }

    @Override
    public String getHelp() {
        return "Generates a Java JAXRS Server application based on Jersey framework.";
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if ("null".equals(property.example)) {
            property.example = null;
        }

        // --- Add imports for Jackson ----------
        if (!BooleanUtils.toBoolean(model.isEnum)) {
            model.imports.add("JsonProperty");

            if (BooleanUtils.toBoolean(model.hasEnums)) {
                model.imports.add("JsonValue");
            }
        }

        // --- Imports for Swagger2 ------------- 
        if (this.isLibrary(LIBRARY_JERSEY3)) {
            model.imports.add("Schema");
        }

    }

    @Override
    public void processOpts() {
        super.processOpts();

        // use default library if unset
        if (StringUtils.isEmpty(library)) {
            this.setLibrary(DEFAULT_JERSEY_LIBRARY);

        } else if (this.isLibrary(LIBRARY_JERSEY3)) {
            // --- Ensure to use Jakarta for jersey3 ----
            this.setUseJakartaEe(true);
            additionalProperties.put(USE_JAKARTA_EE, true);
            this.applyJakartaPackage();
            // --- Set Swagger2 annotations ---------------   
            annotationLibrary = AnnotationLibrary.SWAGGER2;

        }

        convertPropertyToStringAndWriteBack(CodegenConstants.IMPL_FOLDER, value -> implFolder = value);
        if ("joda".equals(dateLibrary)) {
            supportingFiles.add(new SupportingFile("JodaDateTimeProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JodaDateTimeProvider.java"));
            supportingFiles.add(new SupportingFile("JodaLocalDateProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JodaLocalDateProvider.java"));
        } else if (dateLibrary.startsWith("java8")) {
            supportingFiles.add(new SupportingFile("OffsetDateTimeProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "OffsetDateTimeProvider.java"));
            supportingFiles.add(new SupportingFile("LocalDateProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "LocalDateProvider.java"));
        }

        supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml")
                .doNotOverwrite());
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md")
                .doNotOverwrite());
        supportingFiles.add(new SupportingFile("ApiException.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiException.java"));
        supportingFiles.add(new SupportingFile("ApiOriginFilter.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiOriginFilter.java"));
        supportingFiles.add(new SupportingFile("ApiResponseMessage.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiResponseMessage.java"));
        supportingFiles.add(new SupportingFile("NotFoundException.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "NotFoundException.java"));
        supportingFiles.add(new SupportingFile("jacksonJsonProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JacksonJsonProvider.java"));
        supportingFiles.add(new SupportingFile("RFC3339DateFormat.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "RFC3339DateFormat.java"));
        supportingFiles.add(new SupportingFile("bootstrap.mustache", (implFolder + '/' + apiPackage).replace(".", "/"), "Bootstrap.java")
                .doNotOverwrite());
        supportingFiles.add(new SupportingFile("web.mustache", ("src/main/webapp/WEB-INF"), "web.xml")
                .doNotOverwrite());
        supportingFiles.add(new SupportingFile("StringUtil.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "StringUtil.java"));

        // JsonNullable is not implemented for this generator
        openApiNullable = false;
    }


    @Override
    public ModelsMap postProcessModelsEnum(ModelsMap objs) {
        objs = super.postProcessModelsEnum(objs);

        //Add imports for Jackson
        List<Map<String, String>> imports = objs.getImports();
        for (ModelMap mo : objs.getModels()) {
            CodegenModel cm = mo.getModel();
            // for enum model
            if (Boolean.TRUE.equals(cm.isEnum) && cm.allowableValues != null) {
                cm.imports.add(importMapping.get("JsonValue"));
                Map<String, String> item = new HashMap<String, String>();
                item.put("import", importMapping.get("JsonValue"));
                imports.add(item);
            }
        }

        return objs;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        objs = super.postProcessOperationsWithModels(objs, allModels);
        removeImport(objs, "java.util.List");
        return objs;
    }

}
