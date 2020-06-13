package toolkit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exceptions.FormatInvalidException;

public class BedManager {

  private List<BedRecord> records = new ArrayList<>();

  public BedManager(String filePath) {
    this.loadBedfile(filePath);
  }

  public int bedRecordCnt() {
    return this.records.size();
  }

  public BedRecord getRecord(int index) {
    return this.records.get(index);
  }

  @Override
  public String toString() {
    String str = "";
    str += "chrom\ttransBegin\ttransEnd\tregionName\tgreyLevel\tstrand\t" +
        "thickBegin\tthickEnd\tRGB\tblockCount\tblocksLength\tblocksOffset\n";
    for (int i = 0; i < this.records.size(); i++) {
      str += this.records.get(i).toString() + "\n";
    }
    return str;
  }

  public void loadBedfile(String filePath) {
    try {
      Scanner scanner = new Scanner(new FileInputStream(filePath));
      String line = "";
      while (scanner.hasNextLine()) {
        line = scanner.nextLine();
        try {
          BedRecord record = new BedRecord(line);
          this.records.add(record);
        } catch (FormatInvalidException e) {
          e.printStackTrace();
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

}
