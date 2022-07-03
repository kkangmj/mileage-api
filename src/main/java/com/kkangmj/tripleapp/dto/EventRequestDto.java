package com.kkangmj.tripleapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class EventRequestDto {
  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonProperty("type")
  private EventType eventType;

  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonProperty("action")
  private EventAction eventAction;

  @NotBlank
  @Pattern(regexp = "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
  private String reviewId;

  @NotBlank
  @Size(min = 1, max = 1000)
  private String content;

  @NotNull
  private List<
          @Pattern(
              regexp =
                  "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
          String>
      attachedPhotoIds;

  @NotBlank
  @Pattern(regexp = "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
  private String userId;

  @NotBlank
  @Pattern(regexp = "[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}")
  private String placeId;
}
