package ru.clevertec.ecl.giftcertificates.swagger;

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
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;

import java.util.List;

@Tag(name = "Order", description = "The Order Api")
public interface OrderSwagger {

    @Operation(summary = "Make an Order fot User.", tags = "Order",
            requestBody = @RequestBody(description = "Enter User id with GiftCertificates ids, what you want to buy",
                    content = @Content(schema = @Schema(implementation = MakeAnOrderRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "user_id": 2,
                                      "gift_ids": [
                                        1,
                                        2
                                      ]
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class), examples = @ExampleObject("""
                            {
                              "id": 2,
                              "price": 25.6,
                              "purchase_time": "2023-05-08T18:37:40:489",
                              "last_addition_time": "2023-05-08T18:37:40:489",
                              "gift_certificates": [
                                {
                                  "id": 1,
                                  "name": "Rick",
                                  "description": "Vabulabudabda",
                                  "price": 25.6,
                                  "duration": 7,
                                  "create_date": "2023-04-23T14:30:59:093",
                                  "last_update_date": "2023-04-23T14:30:59:093",
                                  "tags": [
                                    {
                                      "id": 1,
                                      "name": "Pepsi"
                                    },
                                    {
                                      "id": 2,
                                      "name": "Fanta"
                                    }
                                  ]
                                }
                              ]
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
                                          "fieldName": "giftIds[1]",
                                          "message": "GiftCertificate ID must be greater than 0"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<OrderResponse> makeAnOrder(MakeAnOrderRequest request);

    @Operation(summary = "Find all Orders by User id with pagination. Page max size = 20", tags = "Order", parameters = {
            @Parameter(name = "id", description = "Enter your User id here", example = "2"),
            @Parameter(name = "page", description = "Enter your page number here", example = "0"),
            @Parameter(name = "size", description = "Enter your page size here", example = "10"),
            @Parameter(name = "sortBy", description = "Enter your sort by(id, price, purchaseTime or lastAdditionTime) here",
                    example = "price")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 4,
                                 "price": 89.33,
                                 "purchase_time": "2023-05-08T18:40:37:638",
                                 "last_addition_time": "2023-05-08T18:40:37:638",
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
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation pagination error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorMessage": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "fieldName": "sortBy",
                                          "message": "Acceptable values are only: id, price, purchaseTime, lastAdditionTime"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<List<OrderResponse>> findAllYourOrders(Long id, @ParameterObject OrderPageRequest request);

    @Operation(summary = "Add some GiftCertificates to your Order.", tags = "Order",
            requestBody = @RequestBody(description = "Enter User id and Order id with GiftCertificates ids, what you want to add",
                    content = @Content(schema = @Schema(implementation = UpdateYourOrderRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "user_id": 2,
                                      "order_id": 1,
                                      "gift_ids": [
                                        5
                                      ]
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class), examples = @ExampleObject("""
                            {
                               "id": 4,
                               "price": 144.33,
                               "purchase_time": "2023-05-08T18:40:37:638",
                               "last_addition_time": "2023-05-08T18:48:06:099",
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
                                 },
                                 {
                                   "id": 6,
                                   "name": "Azanti",
                                   "description": "Query",
                                   "price": 55,
                                   "duration": 9,
                                   "create_date": "2023-04-30T14:30:59:093",
                                   "last_update_date": "2023-05-30T14:30:59:093",
                                   "tags": []
                                 }
                               ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No relations between this User and this Order",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoRelationBetweenOrderAndUserException",
                               "errorMessage": "User with ID 2 does not have such Order with ID 4",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "This GiftCertificate is already in the Order",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "AlreadyHaveThisCertificateException",
                              "errorMessage": "Your already have this GiftCertificate",
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
                                          "fieldName": "userId",
                                          "message": "User ID must be greater than 0"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<OrderResponse> addToYourOrder(UpdateYourOrderRequest request);

    @Operation(summary = "Delete Order by User id and Order id.", tags = "Order",
            parameters = {
                    @Parameter(name = "userId", description = "Enter User id here", example = "2"),
                    @Parameter(name = "orderId", description = "Enter Order id here", example = "5")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                               "message": "Order with ID 1 for User with ID 2 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No relations between this User and this Order",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoRelationBetweenOrderAndUserException",
                              "errorMessage": "User with ID 2 does not have such Order with ID 5",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> deleteYourOrder(@RequestParam Long userId, Long orderId);

}
