package com.nklmish.boundary.company;

import com.nklmish.boundary.company.form.CompanyForm;
import com.nklmish.control.company.CompanyManager;
import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.company.Company;
import com.nklmish.repository.company.CompanyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nklmish.boundary.api.ApiConstant.Documentation.PAGINATED_RESULTS;
import static com.nklmish.boundary.api.ApiConstant.Format.APPLICATION_HAL_JSON;
import static com.nklmish.boundary.api.ApiConstant.Pagination.DEFAULT_RETURN_RECORD_COUNT;
import static com.nklmish.boundary.api.ApiConstant.Urls.COMPANIES;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = COMPANIES, produces = APPLICATION_HAL_JSON)
@Api(value = COMPANIES, description = "Manage companies", produces = APPLICATION_HAL_JSON, consumes = APPLICATION_JSON_VALUE)
public class CompanyController {

    public static final String SELF = "self";
    private CompanyRepository companyRepository;
    private CompanyResourceAssembler customerResourceAssembler;
    private CompanyManager companyManager;

    @Autowired
    public CompanyController(CompanyRepository companyRepository,
                             CompanyResourceAssembler customerResourceAssembler,
                             CompanyManager companyManager) {
        this.companyRepository = companyRepository;
        this.customerResourceAssembler = customerResourceAssembler;
        this.companyManager = companyManager;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(companyManager.getCompanyRequestValidator());
    }

    @ApiOperation(value = "Create new company", nickname = "createCompany",
            response = CompanyResource.class
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Validation error"),
    })
    @RequestMapping(method = POST)
    public HttpEntity<CompanyResource> createCompany(
            @Valid @RequestBody CompanyForm companyForm) {
        Company company = getCompany(companyForm);
        company = companyRepository.save(company);
        return new ResponseEntity<>(customerResourceAssembler.toResource(company), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all companies", nickname = "getCompanies",
            notes = "List all available companies " + PAGINATED_RESULTS,
            response = CompanyResource.class
    )
    @RequestMapping(method = GET, produces = APPLICATION_HAL_JSON)
    public HttpEntity<PagedResources<CompanyResource>> getCompanies(
            @PageableDefault(size = DEFAULT_RETURN_RECORD_COUNT, page = 0) Pageable pageable,
            PagedResourcesAssembler<Company> assembler) {

        Page<Company> companies = companyRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(companies, customerResourceAssembler), HttpStatus.OK);
    }

    @ApiOperation(value = "Get company by id", nickname = "getCompanyById",
            notes = "Get details for a particular company",
            response = CompanyResource.class
    )
    @ApiResponses({
            @ApiResponse(code = 404, message = "no company found")
    })
    @RequestMapping(method = GET, value = "/{companyId}")
    public HttpEntity<CompanyResource> getCompanyById(
            @PathVariable("companyId") String companyId) throws ResourceNotFoundException {
        Company company = getCompany(companyId);
        return new ResponseEntity<>(customerResourceAssembler.toResource(company), HttpStatus.OK);
    }

    @ApiOperation(value = "Update company info by id", nickname = "updateCompanyById",
            notes = "Update information for a given company",
            response = CompanyResource.class
    )
    @ApiResponses({
            @ApiResponse(code = 404, message = "no company found")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/{companyId}")
    public HttpEntity<Void> updateCompanyById(
            @PathVariable("companyId") String companyId,
            @Valid @RequestBody CompanyForm companyForm) throws ResourceNotFoundException {
        Company company = getCompany(companyId);
        company.setName(companyForm.getName());
        company.setAddress(companyForm.getAddress());
        company.setContact(companyForm.getContact());
        company.setBeneficialOwners(companyForm.getBeneficialOwners());
        companyRepository.save(company);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update beneficial owners", nickname = "patchCompany",
            notes = "Currently support only updating of beneficial owners for a given company",
            response = CompanyResource.class
    )
    @ApiResponses({
            @ApiResponse(code = 404, message = "no company found")
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "{companyId}")
    public HttpEntity<CompanyResource> patchCompany(
            @PathVariable("companyId") String companyId,
            @Valid @RequestBody Map<String, Object> changes) throws ResourceNotFoundException {
        Company company = getCompany(companyId);

        List<BeneficialOwner> owners = companyManager.processBeneficialOwners(changes);
        if (owners != null) {
            company.getBeneficialOwners().addAll(owners);
        }
        company = companyRepository.save(company);

        CompanyResource resource = customerResourceAssembler.toResource(company);
        return new ResponseEntity<>(resource, HttpStatus.OK);

    }

    @ApiOperation(value = "Delete company by id", nickname = "deleteCompanyById",
            response = CompanyResource.class
    )
    @ApiResponses({
            @ApiResponse(code = 404, message = "no company found")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "{companyId}")
    public HttpEntity<Void> deleteCompanyById(@PathVariable("companyId") String companyId) throws ResourceNotFoundException {
        Company company = getCompany(companyId);
        companyRepository.delete(company);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Company getCompany(String companyId) {
        return Optional.ofNullable(companyRepository.findOne(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("no company with id " + companyId + " exists"));
    }

    private Company getCompany(CompanyForm companyForm) {
        return companyManager.processForm(companyForm);
    }
}
