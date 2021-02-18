package lk.wasity_institute.asset.hall.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HallCondition {
  AC("AC"),
  NAC("NO AC"),
  FO("Fans Only");

  private final String hallCondition;
}
