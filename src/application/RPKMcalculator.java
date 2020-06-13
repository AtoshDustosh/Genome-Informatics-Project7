package application;

import java.util.ArrayList;
import java.util.List;

import toolkit.BedManager;
import toolkit.BedRecord;
import toolkit.SamManager;
import toolkit.SamRecord;

public class RPKMcalculator {
  public static final String BED_FILE = "src/data/sacCer3.chrI.bed";
  public static final String SAM_FILE = "src/data/ERR1594329.chrI.sorted.sam";

  private List<Double> RPKM = new ArrayList<>();
  private List<Integer> fellInto = new ArrayList<>();
  private List<Integer> lengths = new ArrayList<>();

  private BedManager bedManager = null;
  private SamManager samManager = null;

  public RPKMcalculator(String bedFile, String samFile) {
    this.bedManager = new BedManager(bedFile);
    this.samManager = new SamManager(samFile);
  }

  public static void main(String[] args) {
    RPKMcalculator rc = new RPKMcalculator(RPKMcalculator.BED_FILE,
        RPKMcalculator.SAM_FILE);
    rc.execute();
    rc.print();
  }

  public void execute() {
    final int bedRecordCnt = this.bedManager.bedRecordCnt();
    final int samRecordCnt = this.samManager.samRecordCnt();

    int depthOfSequencing = samRecordCnt;
    for (int i = 0; i < bedRecordCnt; i++) {
      int readsFellInto = 0;
      int lengthOfGene = 0;
      BedRecord bedRecord = this.bedManager.getRecord(i);
      int[] exonsLength = bedRecord.getBlocksLength();
      int[] exonsOffset = bedRecord.getBlocksOffset();
      List<ExonRegion> exons = new ArrayList<>();
//      System.out.println("i: " + i + ", exonsCnt: " + exonsOffset.length);
      for (int j = 0; j < exonsLength.length; j++) {
//        System.out.println("length of exonj" + j + "..." + exonsLength[j]);
        lengthOfGene += exonsLength[j];
        exons.add(
            new ExonRegion(exonsOffset[j], exonsOffset[j] + exonsLength[j]));
      }
      for (int j = 0; j < samRecordCnt; j++) {
        SamRecord samRecord = this.samManager.getRecord(j);
        int begin = samRecord.getPOS() - 1;
        int end = begin + samRecord.getSEQ().length();
        for (int k = 0; k < exons.size(); k++) {
          ExonRegion region = exons.get(k);
          if (region.contains(begin, end)) {
            readsFellInto++;
          }
        }
      }
      // calculate the RPKM of this gene
      double geneRPKM = 1E9 * readsFellInto
          / (depthOfSequencing * lengthOfGene);
      this.RPKM.add(geneRPKM);
      this.fellInto.add(readsFellInto);
      this.lengths.add(lengthOfGene);
    }
  }

  public void print() {
    System.out.println("N:" + this.samManager.samRecordCnt());
    for (int i = 0; i < this.RPKM.size(); i++) {
      String str = "";
      str += this.bedManager.getRecord(i).toString();
      str += "............(C:" + this.fellInto.get(i) + ")";
      str += ".....(L:" + this.lengths.get(i) + ")";
      str += ".....R:" + this.RPKM.get(i);
      System.out.println(str);
    }
  }

  class ExonRegion {
    private int begin;
    private int end;

    public ExonRegion(int begin, int end) {
      this.begin = begin;
      this.end = end;
    }

    public int getBegin() {
      return this.begin;
    }

    public int getEnd() {
      return this.end;
    }

    public boolean contains(int begin, int end) {
      if (begin < end && begin >= this.begin && end <= this.end) {
        return true;
      } else {
        return false;
      }
    }

  }
}
