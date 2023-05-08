package ru.clevertec.ecl.giftcertificates.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;

import java.util.List;

@Tag(name = "User", description = "The User Api")
public interface UserSwagger {

    @Operation(summary = "Find User by id.", tags = "User",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class), examples = @ExampleObject("""
                            {
                               "id": 4,
                               "username": "Sid",
                               "orders": [
                                 {
                                   "id": 4,
                                   "price": 89.33,
                                   "purchase_time": "2023-05-08T18:31:25:238",
                                   "last_addition_time": "2023-05-08T18:31:25:238",
                                   "gift_certificates": [
                                     {
                                       "id": 5,
                                       "name": "Donatello",
                                       "description": "Zap",
                                       "price": 89.33,
                                       "duration": 1,
                                       "create_date": "2023-04-25T14:30:59:093",
                                       "last_update_date": "2023-04-30T14:30:59:093",
                                       "tags": []
                                     }
                                   ]
                                 }
                               ]
                             }
                            """))),
            @ApiResponse(responseCode = "404", description = "No User with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchUserException",
                               "errorMessage": "User with ID 6 does not exist",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<UserDto> findById(Long id);

    @Operation(summary = "Find all Users with pagination. Page max size = 20", tags = "User", parameters = {
            @Parameter(name = "page", description = "Enter your page number here", example = "0"),
            @Parameter(name = "size", description = "Enter your page size here", example = "10"),
            @Parameter(name = "sortBy", description = "Enter your sort by(id or username) here", example = "id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 1,
                                 "username": "Bully",
                                 "orders": []
                               },
                               {
                                 "id": 2,
                                 "username": "Jim",
                                 "orders": [
                                   {
                                     "id": 1,
                                     "price": 75.3,
                                     "purchase_time": "2023-05-08T18:34:02:293",
                                     "last_addition_time": "2023-05-08T18:34:02:293",
                                     "gift_certificates": [
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
                                             "id": 3,
                                             "name": "Sprite"
                                           },
                                           {
                                             "id": 5,
                                             "name": "7-Up"
                                           }
                                         ]
                                       }
                                     ]
                                   }
                                 ]
                               },
                               {
                                 "id": 3,
                                 "username": "Pavel",
                                 "orders": []
                               },
                               {
                                 "id": 4,
                                 "username": "Sid",
                                 "orders": []
                               },
                               {
                                 "id": 5,
                                 "username": "Peter",
                                 "orders": []
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
                                           "fieldName": "page",
                                           "message": "Page must be greater than or equal to 0"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<List<UserDto>> findAll(@ParameterObject UserPageRequest request);

}
