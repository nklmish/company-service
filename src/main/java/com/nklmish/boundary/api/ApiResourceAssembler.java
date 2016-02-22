/* 
 * Copyright 2013-2015 JIWHIZ Consulting Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nklmish.boundary.api;

import com.nklmish.boundary.api.ApiConstant.TemplatePlaceHolders;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ApiResourceAssembler {

    public ApiResource toResource() {
        ApiResource resource = new ApiResource();
        String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
        resource.add(linkTo(methodOn(ApiController.class).getApi()).withSelfRel());

        Link companies = new Link(
                new UriTemplate((baseUri + ApiConstant.Urls.COMPANIES),
                        new TemplateVariables(
                                new TemplateVariable(TemplatePlaceHolders.PAGE, REQUEST_PARAM),
                                new TemplateVariable(TemplatePlaceHolders.SIZE, REQUEST_PARAM),
                                new TemplateVariable(TemplatePlaceHolders.SORT, REQUEST_PARAM))),
                ApiConstant.HalResourceNames.COMPANIES);

        resource.add(companies);
        return resource;
    }
}
