package ru.clevertec.ecl.giftcertificates.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;

import java.util.List;

@Tag(name = "GiftCertificate", description = "The GiftCertificate Api")
public interface GiftCertificateOpenApi {

    @Operation(summary = "Find GiftCertificate by id.", tags = "GiftCertificate",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GiftCertificate retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GiftCertificateResponse.class), examples = @ExampleObject("""
                            {
                              "id": 3,
                              "name": "Pechkin",
                              "description": "abda",
                              "price": 75.3,
                              "duration": 5,
                              "create_date": "2023-04-23T14:30:59:093",
                              "last_update_date": "2023-04-23T14:30:59:093",
                              "tags": [
                                {
                                  "id": 5,
                                  "name": "7-Up"
                                }
                              ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No GiftCertificate with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchGiftCertificateException",
                               "errorMessage": "GiftCertificate with ID 8 does not exist",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<GiftCertificateResponse> findById(Long id);

    @Operation(summary = """
            Finds all GiftCertificates by certain parameters(all params are optional and can be used in conjunction).
            If there are no parameters, it works as a find all method. You can use all the parameters or several or none.
            """, tags = "GiftCertificate",
            parameters = {
                    @Parameter(name = "tagName", description = "Enter Tag name here", example = "Pepsi"),
                    @Parameter(name = "part", description = "Enter part of name or description here", example = "Little"),
                    @Parameter(name = "sortBy", description = "Enter sort by date or by name here", example = "date"),
                    @Parameter(name = "order", description = "Enter order for sorting(ASC or DESC) here", example = "desc")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of GiftCertificates retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GiftCertificateResponse.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 7,
                                 "name": "Sara",
                                 "description": "Little",
                                 "price": 33.29,
                                 "duration": 4,
                                 "create_date": "2023-05-08T14:30:59:093",
                                 "last_update_date": "2023-05-09T14:30:59:093",
                                 "tags": [
                                   {
                                     "id": 1,
                                     "name": "Pepsi"
                                   },
                                   {
                                     "id": 2,
                                     "name": "Fanta"
                                   },
                                   {
                                     "id": 5,
                                     "name": "7-Up"
                                   }
                                 ]
                               }
                            ]
                            """)))
    })
    ResponseEntity<List<GiftCertificateResponse>> findAllWithTags(String tagName,
                                                                  String part,
                                                                  String sortBy,
                                                                  String order);

    @Operation(summary = "Save new GiftCertificate.", tags = "GiftCertificate",
            requestBody = @RequestBody(description = "RequestBody for GiftCertificate",
                    content = @Content(schema = @Schema(implementation = GiftCertificateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "name": "Gift",
                                      "description": "Very Big",
                                      "price": 10.25,
                                      "duration": 3,
                                      "tags": [
                                        {
                                          "name": "Coca-cola"
                                        },
                                        {
                                          "name": "Sprite"
                                        }
                                      ]
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "GiftCertificate saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GiftCertificateResponse.class), examples = @ExampleObject("""
                            {
                               "id": 8,
                               "name": "Gift",
                               "description": "Very Big",
                               "price": 10.25,
                               "duration": 3,
                               "create_date": "2023-05-08T18:05:40:280",
                               "last_update_date": "2023-05-08T18:05:40:280",
                               "tags": [
                                 {
                                   "id": 8,
                                   "name": "Coca-cola"
                                 },
                                 {
                                   "id": 9,
                                   "name": "Sprite"
                                 }
                               ]
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "While saving GiftCertificate with two or more Tags with the same name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoTagWithTheSameNameException",
                              "errorMessage": "There should be no tags with the same name!",
                              "errorCode": "406 NOT_ACCEPTABLE"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "tags[1].name",
                                           "message": "Name cannot be blank"
                                         },
                                         {
                                           "fieldName": "name",
                                           "message": "Name cannot be blank"
                                         },
                                         {
                                           "fieldName": "price",
                                           "message": "Price must be greater than or equal to 0.01"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<GiftCertificateResponse> save(GiftCertificateRequest giftCertificateRequest);

    @Operation(summary = "Update GiftCertificate price and duration (or something one) by id.", tags = "GiftCertificate",
            requestBody = @RequestBody(description = "RequestBody for GiftCertificate",
                    content = @Content(schema = @Schema(implementation = PriceDurationUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "id": 2,
                                      "price": 777.56,
                                      "duration": 11
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GiftCertificate updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GiftCertificateResponse.class), examples = @ExampleObject("""
                            {
                               "id": 3,
                               "name": "Pechkin",
                               "description": "abda",
                               "price": 777.56,
                               "duration": 11,
                               "create_date": "2023-04-23T14:30:59:093",
                               "last_update_date": "2023-05-08T18:13:43:564",
                               "tags": [
                                 {
                                   "id": 5,
                                   "name": "7-Up"
                                 }
                               ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not GiftCertificate with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchGiftCertificateException",
                               "errorMessage": "GiftCertificate with ID 15 does not exist",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "duration",
                                           "message": "Duration must be greater than 0"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<GiftCertificateResponse> update(PriceDurationUpdateRequest request);

    @Operation(summary = "Delete GiftCertificate by id.", tags = "GiftCertificate",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GiftCertificate deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "GiftCertificate with ID 3 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not GiftCertificate with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchGiftCertificateException",
                              "errorMessage": "There is no GiftCertificate with ID 3 to delete",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> delete(Long id);

}
