package lk.wasityInstitute.asset.hall.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HallCondition {
  AC("Ac"),
  NAC("Non Ac");

  private final String hallCondition;

}
