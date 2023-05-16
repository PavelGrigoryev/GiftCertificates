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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.TagPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;

import java.util.List;

@Tag(name = "Tag", description = "The Tag Api")
public interface TagOpenApi {

    @Operation(summary = "Find Tag by id.", tags = "Tag",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "id": 2,
                              "name": "Fanta"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Tag with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchTagException",
                              "errorMessage": "Tag with ID 9 does not exist",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<TagDto> findById(Long id);

    @Operation(summary = "Find all Tags with pagination. Page max size = 20", tags = "Tag", parameters = {
            @Parameter(name = "page", description = "Enter your page number here", example = "0"),
            @Parameter(name = "size", description = "Enter your page size here", example = "10"),
            @Parameter(name = "sortBy", description = "Enter your sort by(id or name) here", example = "id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Tags retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            [
                              {
                                "id": 1,
                                "name": "Pepsi"
                              },
                              {
                                "id": 2,
                                "name": "Fanta"
                              },
                              {
                                "id": 3,
                                "name": "Sprite"
                              },
                              {
                                "id": 4,
                                "name": "Cola"
                              },
                              {
                                "id": 5,
                                "name": "7-Up"
                              }
                            ]
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation pagination error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorMessage": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "fieldName": "size",
                                          "message": "Size must be greater than or equal 1"
                                        },
                                        {
                                          "fieldName": "sortBy",
                                          "message": "Acceptable values are only: id or name"
                                        },
                                        {
                                          "fieldName": "page",
                                          "message": "Page must be greater than or equal to 0"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<List<TagDto>> findAll(@ParameterObject TagPageRequest request);

    @Operation(summary = "Save new Tag.", tags = "Tag",
            requestBody = @RequestBody(description = "RequestBody for Tag",
                    content = @Content(schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "name": "Surprise"
                            }
                            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "id": 6,
                              "name": "Surprise"
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Tag name is not unique",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoTagWithTheSameNameException",
                              "errorMessage": "Tag name Surprise is already exist! It must be unique!",
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
                                           "fieldName": "name",
                                           "message": "Name cannot be blank"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<TagDto> save(TagDto tagDto);

    @Operation(summary = "Update Tag name by id.", tags = "Tag",
            requestBody = @RequestBody(description = "RequestBody for Tag",
                    content = @Content(schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "id": 3,
                              "name": "Banana"
                            }
                            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "id": 3,
                              "name": "Banana"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not Tag with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchTagException",
                              "errorMessage": "Tag with ID 9 does not exist",
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
                                            "fieldName": "id",
                                            "message": "Tag ID must be greater than 0"
                                          },
                                          {
                                            "fieldName": "name",
                                            "message": "Name cannot be blank"
                                          }
                                        ]
                                    }
                                    """)))
    })
    ResponseEntity<TagDto> update(TagDto tagDto);

    @Operation(summary = "Delete Tag by id.", tags = "Tag",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Tag with ID 3 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not Tag with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchTagException",
                               "errorMessage": "There is no Tag with ID 3 to delete",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> delete(Long id);

    @Operation(summary = "Find the most widely used Tag of a User with the highest cost of all Orders.", tags = "Tag",
            parameters = @Parameter(name = "userId", description = "Enter User id here", example = "3"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDto.class), examples = @ExampleObject("""
                            {
                              "id": 2,
                              "name": "Fanta"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not Tag with this User id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchTagException",
                              "errorMessage": "There is no Tags in database with User ID 3 connection",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<TagDto> findTheMostWidelyUsedWithTheHighestCost(Long userId);

}
