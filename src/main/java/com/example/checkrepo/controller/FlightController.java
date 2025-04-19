package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.exception.ErrorObject;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.TypeMissMatchException;
import com.example.checkrepo.service.FlightServiceImpl;
import com.example.checkrepo.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.QueryParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flight Management", description = "Operations related to flight management")
public class FlightController {
    private final FlightServiceImpl flightService;
    private final UserServiceImpl userService;

    @PostMapping()
    @Operation(summary = "Add a new flight", description = "Creates a new flight in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
            description = "Flight created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FlightDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TypeMissMatchException.class))),
        @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class)))
    })
    public void addNewFlight(
            @Parameter(description = "Flight data transfer object", required = true)
            @Valid @RequestBody FlightDto flightAdd) {
        flightService.createDbFlight(flightAdd);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID", description = "Retrieves flight details by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flight"),
        @ApiResponse(responseCode = "400", description = "Invalid flight ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MethodArgumentTypeMismatchException.class))),
        @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorObject.class)))
    })
    public Optional<FlightDto> getById(
            @Parameter(description = "ID of the flight to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return flightService.findById(id);
    }

    @GetMapping()
    @Operation(summary = "Get all flights",
            description = "Retrieves a list of all available flights")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flights"),
        @ApiResponse(responseCode = "404", description = "Fligth list is empty",
                    content = @Content(schema = @Schema(implementation =  ErrorObject.class))),
    })
    public List<FlightDto> displayAllFlight() {
        return flightService.displayAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a flight",
            description = "Removes a flight from the system by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Flight successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid flight ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IncorrectInputException.class))),
        @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorObject.class)))
    })
    public void deleteFlight(
            @Parameter(description = "ID of the flight to delete", required = true, example = "1")
            @PathVariable Long id) {
        flightService.deleteFlight(id);
    }

    @PutMapping("/assignFlightToUser")
    @Operation(summary = "Assign flight to user",
            description = "Links an existing flight to a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                    description = "Flight successfully assigned to user"),
        @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "flight or user were not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorObject.class)))
    })
    public UserDto addingNewFlight(
            @Parameter(description = "ID of the flight to assign", required = true, example = "1")
            @QueryParam("flightId") Long flightId,
            @Parameter(description = "ID of the user to assign to", required = true, example = "1")
            @QueryParam("userId") Long userId) {
        try {
            return userService.addingNewFlight(flightId, userId);
        } catch (IncorrectInputException e) {
            throw new BadRequestException("ID must be a valid number");
        }
    }

    @GetMapping("/startNative")
    @Operation(summary = "Find flights by starting point (Native SQL)",
            description = "Searches for flights with specified starting point using native query")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved matching flights"),
        @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404",
                    description = "No flights found for the starting point"),
    })
    public List<FlightDto> getBySameStartNative(
            @Parameter(description = "Starting point to search for",
                    required = true, example = "New York")
            @QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDestNative(flightStart);
    }

    @GetMapping("/startJPQL")
    @Operation(summary = "Find flights by starting point (JPQL)",
            description = "Searches for flights with specified starting point using JPQL query")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved matching flights"),
        @ApiResponse(responseCode = "404",
                    description = "Nothing was found",
                    content = @Content(schema = @Schema(implementation = ErrorObject.class))),
    })
    public List<FlightDto> getBySameStartJpql(
            @Parameter(description = "Starting point to search for",
                    required = true, example = "London")
            @QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDestJpql(flightStart);
    }

    @GetMapping("/fromExcel")
    @Operation(summary = "Import flights from Excel",
            description = "Imports flight data from an Excel file into the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Flights imported successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid company ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IncorrectInputException.class))),
    })
    public void getFromExcel() throws IOException {
        flightService.getFromExcel();
    }

    @PostMapping("/bulkOp")
    @Operation(summary = "Perform bulk operation on flights",
            description = "Executes a bulk operation on flights based on company names")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                    description = "Bulk operation completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    public List<FlightDto> bulkOperation(
            @Parameter(description = "List of company names for bulk operation", required = true)
            @RequestBody List<String> nameOfCompanies) {
        return flightService.bulkOperation(nameOfCompanies);
    }
}