package lk.wasity_institute.asset.common_asset.model;

import lk.wasity_institute.asset.time_table.entity.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateTimeTable {
  private List< TimeTable > timeTables;
  private LocalDate date;
}
