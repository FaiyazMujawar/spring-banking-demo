package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "API Health Response model")
public class ApiHealthResponse extends SuccessfulResponse {
  @ApiModelProperty(name = "message", notes = "Health status")
  private final String message;

  @ApiModelProperty(name = "message", notes = "URL of the repository")
  private final String repositoryURL;

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the repositoryURL
   */
  public String getRepositoryURL() {
    return repositoryURL;
  }

  /**
   * @param message
   * @param repositoryURL
   */
  public ApiHealthResponse(String message, String repositoryURL) {
    this.message = message;
    this.repositoryURL = repositoryURL;
  }
}
