package toolkit;

import exceptions.FormatInvalidException;

public class BedRecord {
  public static final String STR_UNSET = ""; // a string variable is not set
  public static final int NUM_UNSET = -1; // a number variable is not set

  public static final int PLUS_STRAND = 1;
  public static final int MINUS_STRAND = 0;

  private String chromesome = STR_UNSET;
  private int transRegionBegin = NUM_UNSET;
  private int transRegionEnd = NUM_UNSET;
  private String regionName = STR_UNSET;
  private int greyLevel = NUM_UNSET;
  private int strand = NUM_UNSET;
  private int thickBegin = NUM_UNSET;
  private int thickEnd = NUM_UNSET;
  private String RGB = STR_UNSET;
  private int blockCount = NUM_UNSET;
  private int[] blocksLength = null;
  private int[] blocksOffset = null;

  public BedRecord(String bedLine) throws FormatInvalidException {
    this.parseBedLine(bedLine);
  }

  public boolean hasRegionName() {
    if (this.regionName.equals(STR_UNSET)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasStrand() {
    if (this.strand == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasThick() {
    if (this.thickBegin == NUM_UNSET || this.thickEnd == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasRGB() {
    if (this.RGB.equals(STR_UNSET)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasBlock() {
    if (this.blockCount == NUM_UNSET) {
      return false;
    } else {
      return true;
    }
  }

  public String getChromesome() {
    return this.chromesome;
  }

  public long getTransRegionBegin() {
    return this.transRegionBegin;
  }

  public long getTransRegionEnd() {
    return this.transRegionEnd;
  }

  public String getRegionName() {
    return this.regionName;
  }

  public int getGreyLevel() {
    return this.greyLevel;
  }

  public int getStrand() {
    return this.strand;
  }

  public long getThickBegin() {
    return this.thickBegin;
  }

  public long getThickEnd() {
    return this.thickEnd;
  }

  public String getRGB() {
    return this.RGB;
  }

  public int getBlockCount() {
    return this.blockCount;
  }

  public int getBlockLength(int blockIndex) {
    if (blockIndex < this.blockCount) {
      return this.blocksLength[blockIndex];
    } else {
      return -1;
    }
  }

  public int[] getBlocksLength() {
    return this.blocksLength;
  }

  public int getBlockOffset(int blockIndex) {
    if (blockIndex < this.blockCount) {
      return this.blocksOffset[blockIndex];
    } else {
      return -1;
    }
  }

  public int[] getBlocksOffset() {
    return this.blocksOffset;
  }

  @Override
  public String toString() {
    String str = "";
    str += this.chromesome + "\t" + this.transRegionBegin + "\t"
        + this.transRegionEnd + "\t" + this.regionName + "\t" + this.greyLevel
        + "\t";
    if (this.strand == BedRecord.PLUS_STRAND) {
      str += "+";
    } else {
      str += "-";
    }
    str += "\t" + this.thickBegin +
        "\t" + this.thickEnd + "\t" + this.RGB + "\t" + this.blockCount + "\t"
        + this.blocksLength + "\t" + this.blocksOffset;
    return str;
  }

  private void parseBedLine(String line) throws FormatInvalidException {
    String[] tokens = line.split("\t");
    if (tokens.length < 3) {
      // a valid BED line must be filled with over 3 fields
      return;
    }

    try {
      this.chromesome = tokens[0];
      this.transRegionBegin = Integer.valueOf(tokens[1]);
      this.transRegionEnd = Integer.valueOf(tokens[2]);
    } catch (NumberFormatException e) {
      throw new FormatInvalidException("invalid bed line - " + line);
    }
    if (tokens.length >= 4) {
      this.regionName = tokens[3];
    }
    if (tokens.length >= 5) {
      try {
        this.greyLevel = Integer.valueOf(tokens[4]);
      } catch (NumberFormatException e) {
        this.greyLevel = 0;
      }
    }
    if (tokens.length >= 6) {
      if (tokens[5].equals("+")) {
        this.strand = BedRecord.PLUS_STRAND;
      } else if (tokens[5].equals("-")) {
        this.strand = BedRecord.MINUS_STRAND;
      } else {
        throw new FormatInvalidException("invalid bed line - " + line);
      }
    }
    if (tokens.length >= 8) {
      try {
        this.thickBegin = Integer.valueOf(tokens[6]);
        this.thickEnd = Integer.valueOf(tokens[7]);
      } catch (NumberFormatException e) {
        throw new FormatInvalidException("invalid bed line - " + line);
      }
    }
    if (tokens.length >= 9) {
      this.RGB = tokens[8];
    }
    if (tokens.length >= 10) {
      try {
        this.blockCount = Integer.valueOf(tokens[9]);
        if (this.blockCount == 0) {
          return;
        } else {
          if (tokens.length == 12) {
            String[] strblockLengths = tokens[10].split(",");
            String[] strblockOffsets = tokens[11].split(",");
            int[] blockLengths = new int[strblockLengths.length];
            int[] blockOffsets = new int[strblockOffsets.length];
            for (int i = 0; i < this.blockCount; i++) {
              blockLengths[i] = Integer.valueOf(strblockLengths[i]);
              blockOffsets[i] = Integer.valueOf(strblockOffsets[i]);
            }
            this.blocksLength = blockLengths;
            this.blocksOffset = blockOffsets;
          }
        }
      } catch (NumberFormatException e) {
        throw new FormatInvalidException("invalid bed line - " + line);
      }
    }
//    System.out.println("line: " + line);
  }

}
