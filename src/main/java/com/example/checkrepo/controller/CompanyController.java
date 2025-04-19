package com.example.checkrepo.controller;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.exception.ErrorObject;
import com.example.checkrepo.exception.GlobalExceptionHandler;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.service.CompanyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company Management", description = "Operations related to work with companies")
@AllArgsConstructor
public class CompanyController {
    private CompanyServiceImpl companyService;

    @PostMapping()
    @Operation(
            summary = "Create a new company",
            description = "Registers a new flight company in the system",
            responses = {
                @ApiResponse(responseCode = "201", description = "Company created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    void newCompany(@Parameter(description = "Company data transfer object", required = true)
                    @RequestBody CompanyDto companyDto) {
        companyService.addFlightCompany(companyDto);
    }

    @PutMapping("/assignFlight")
    @Operation(
            summary = "Assign flight to company",
            description = "Links an existing flight to a specific company",
            responses = {
                @ApiResponse(responseCode = "201",
                        description = "Flight successfully assigned to company"),
                @ApiResponse(responseCode = "404",
                        description = "Flight or company not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    public Optional<CompanyDto> addFlightToCompany(
            @Parameter(description = "ID of the flight to assign", required = true, example = "1")
            @QueryParam("flightId") Long flightId,
            @Parameter(description = "ID of the target company", required = true, example = "1")
            @QueryParam("companyId") Long companyId) {
        return companyService.addFlightToCompany(flightId, companyId);
    }

    @GetMapping()
    @Operation(
            summary = "Get all companies",
            description = "Retrieves a list of all registered flight companies",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved all companies"),
                @ApiResponse(responseCode = "404",
                        description = "Companies were not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    public List<CompanyDto> displayAll() {
        return companyService.showAll();
    }

    @DeleteMapping("/{companiesId}")
    @Operation(
            summary = "Delete a company",
            description = "Removes a company from the system by its ID",
            responses = {
                @ApiResponse(responseCode = "204", description = "Company successfully deleted"),
                @ApiResponse(responseCode = "400", description = "Invalid company ID",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IncorrectInputException.class))),
                @ApiResponse(responseCode = "404", description = "Company not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ObjectNotFoundException.class))),
            }
    )
    public void deleteById(
            @Parameter(description = "ID of the company to delete", required = true, example = "1")
            @PathVariable Long companiesId) {
        companyService.deleteCompany(companiesId);
    }

    @GetMapping("/findByFlightNative")
    @Operation(
            summary = "Find companies by destination (Native SQL)",
            description = "Searches for companies that have flights "
                    + "the specified destination using native SQL query",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved matching companies"),
                @ApiResponse(responseCode = "404",
                        description = "No companies found for the destination",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    public List<CompanyDto> findByCompanyIdNative(
            @Parameter(description = "Destination name to search for",
                    required = true, example = "New York")
            @RequestParam("startDestinationName") String endDestination) {
        return companyService.getCompanyFlightsNative(endDestination);
    }

    @GetMapping("/findByFlightJPQL")
    @Operation(
            summary = "Find companies by destination (JPQL)",
            description = "Searches for companies to the specified destination using JPQL query",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved matching companies"),
                @ApiResponse(responseCode = "404",
                        description = "No companies found for the destination",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    public List<CompanyDto> findByCompanyIdJpql(
            @Parameter(description = "Destination name to search for",
                    required = true, example = "London")
            @RequestParam("startDestinationName") String endDest) {

        return companyService.getCompanyFlightsJpql(endDest);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get company by ID",
            description = "Retrieves details of a specific company by its ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved company"),
                @ApiResponse(responseCode = "400", description = "Invalid company ID",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IncorrectInputException.class))),
                @ApiResponse(responseCode = "404", description = "Company not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
            }
    )
    public CompanyDto findById(
            @Parameter(description = "ID of the company to retrieve",
                    required = true, example = "1")
            @PathVariable Long id) {
        try {
            return companyService.findById(id);
        } catch (IncorrectInputException e) {
            throw e;
        }
    }
}
