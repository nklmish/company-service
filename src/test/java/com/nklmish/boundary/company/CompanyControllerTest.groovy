package com.nklmish.boundary.company

import com.nklmish.SpringSpecification
import com.nklmish.boundary.company.form.CompanyForm
import com.nklmish.entity.address.Address
import com.nklmish.entity.beneficial.BeneficialOwner
import com.nklmish.entity.company.Company
import com.nklmish.entity.contact.Contact
import com.nklmish.repository.company.CompanyRepository
import com.nklmish.boundary.api.ApiConstant
import org.hamcrest.Matchers
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView

import static com.nklmish.boundary.api.ApiConstant.Format.APPLICATION_HAL_JSON
import static com.nklmish.boundary.api.ApiConstant.RequestParamsNames.Company.BENEFICIAL_OWNERS
import static com.nklmish.boundary.api.ApiConstant.Pagination.DEFAULT_RETURN_RECORD_COUNT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CompanyControllerTest extends SpringSpecification {

    static final String BASE_URL = ApiConstant.Urls.COMPANIES

    @Autowired
    CompanyController companyController

    @Autowired
    CompanyRepository companyRepository

    MockMvc mockMvc

    HttpMessageConverter mappingJackson2HttpMessageConverter;

    Address applicantAddress

    List<BeneficialOwner> owners

    Contact contact

    CompanyForm applicationForm

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Arrays.asList(converters)
                .find { it instanceof MappingJackson2HttpMessageConverter }

        Assert.assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                new PagedResourcesAssemblerArgumentResolver(new HateoasPageableHandlerMethodArgumentResolver(), null))
                .setViewResolvers(new ViewResolver() {
            @Override
            public View resolveViewName(String viewName, Locale locale) throws Exception {
                return new MappingJackson2JsonView();
            }
        }).build();

        applicantAddress = new Address(city: 'new york', country: 'usa', locationDescription: 'foo')
        owners = [new BeneficialOwner(name: 'john')]
        contact = new Contact(email: 'foo@bar.foobar', phoneNumber: "123")
        applicationForm = new CompanyForm(name: 'companyX',
                address: applicantAddress,
                beneficialOwners: owners,
                contact: contact
        )
    }

    def "should be able to create a new company"() {
        when:
        def response = mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON).content(json(applicationForm)))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.name').value(applicationForm.name))
                .andExpect(jsonPath('$.address.city').value(applicantAddress.city))
                .andExpect(jsonPath('$.address.country').value(applicantAddress.country))
                .andExpect(jsonPath('$.address.locationDescription').value(applicantAddress.locationDescription))
                .andExpect(jsonPath('$.links').isNotEmpty())
    }

    def "should be able to update an existing company"() {
        given:
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON).content(json(applicationForm)))
        Company company = companyRepository.findAll()[0]
        CompanyForm updateApplicantForm = new CompanyForm(name: "b",
                address: new Address(city: "foo", country: "usa", locationDescription: "aa"),
                beneficialOwners: [new BeneficialOwner(name: "a"), new BeneficialOwner(name: "b")],
                contact: new Contact(email: "john@doe.com", phoneNumber: "123")
        )

        when:
        def response = mockMvc.perform(put(BASE_URL + "/" + company.id).content(json(updateApplicantForm))
                .contentType(APPLICATION_JSON))
        Company entity = companyRepository.findAll()[0]

        then:
        response.andExpect(status().is2xxSuccessful())
        entity.address == updateApplicantForm.address
        entity.beneficialOwners == updateApplicantForm.beneficialOwners
        entity.contact == updateApplicantForm.contact
        entity.name == updateApplicantForm.name

    }


    def "should be able to fetch all existing companies"() {
        given:'list of companies'
        CompanyForm applicationForm1 = applicationForm

        CompanyForm applicationForm2 = new CompanyForm(name: 'companyY',
                address: applicantAddress,
                beneficialOwners: owners,
                contact: contact
        )

        List<CompanyForm> companies = [applicationForm1, applicationForm2]
        companies.each {
            mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON).content(json(applicationForm1)))
        }

        when: 'we ask for all companies'
        def response = mockMvc.perform(get(BASE_URL).contentType(APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.content', Matchers.hasSize(companies.size() - DEFAULT_RETURN_RECORD_COUNT)))
                .andExpect(jsonPath('$.links').isNotEmpty())
        and:
        companyRepository.count() == companies.size()
    }

    def "should be able to retrieve company by id"() {
        given:
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON).content(json(applicationForm)))
        List<Company> companies = companyRepository.findAll()

        when:
        def response = mockMvc.perform(get(BASE_URL + "/" + companies[0].id).contentType(APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.name').value(applicationForm.name))
                .andExpect(jsonPath('$.address.city').value(applicantAddress.city))
                .andExpect(jsonPath('$.address.country').value(applicantAddress.country))
                .andExpect(jsonPath('$.address.locationDescription').value(applicantAddress.locationDescription))
                .andExpect(jsonPath('$.links').isNotEmpty())

        and:
        companies.size() == 1

    }

    def "should be able to update beneficiary owners for a given company"() {
        given:
        CompanyForm applicationForm = new CompanyForm(name: 'companyY',
                address: applicantAddress,
                beneficialOwners: owners,
                contact: contact
        )
        mockMvc.perform(post(BASE_URL + "").contentType(APPLICATION_JSON).content(json(applicationForm)))
        List<Company> companies = companyRepository.findAll()

        and:
        def newOwners = [["name" : "bob"], ["name" : "adam"]]
        Map<String, Object> params = ["${BENEFICIAL_OWNERS}" : newOwners]

        when:
        def updatedResponse = mockMvc.perform(patch(BASE_URL + "/" + companies[0].id).contentType(APPLICATION_JSON).content(json(params)))

        then:
        updatedResponse.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.name').value(applicationForm.name))
                .andExpect(jsonPath('$.beneficialOwners', Matchers.hasSize(applicationForm.beneficialOwners.size() + newOwners.size())))
                .andExpect(jsonPath('$.links').isNotEmpty())
    }

    def "should be able to delete company by id"() {
        given:
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON).content(json(applicationForm)))
        List<Company> companies = companyRepository.findAll()

        when:
        mockMvc.perform(delete(BASE_URL + "/" + companies[0].id).contentType(APPLICATION_JSON))

        then:
        companyRepository.count() == 0
    }

    def String json(Object o) {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, APPLICATION_JSON, mockHttpOutputMessage);

        mockHttpOutputMessage.getBodyAsString();
    }

    void cleanup() {
        companyRepository.deleteAll()
    }
}
