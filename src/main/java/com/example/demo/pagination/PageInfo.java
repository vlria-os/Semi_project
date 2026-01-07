package com.example.demo.pagination;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageInfo {
    private final int pageNum;
    private final int rowBlockCount;
    private final int pageBlockCount;
    private final int totalRowCount;

    private final int startRow;
    private final int endRow;
    private final int totalPageCount;
    private final int startPageNum;
    private final int endPageNum;

    public PageInfo(int pageNum, int rowBlockCount, int pageBlockCount, int totalRowCount){
        this.pageNum=pageNum;
        this.rowBlockCount=rowBlockCount;
        this.pageBlockCount=pageBlockCount;
        this.totalRowCount=totalRowCount;

        this.startRow = (pageNum-1) * rowBlockCount +1;
        this.endRow = this.startRow + rowBlockCount -1;

        this.totalPageCount = (int)Math.ceil(totalRowCount/(double)rowBlockCount);
        this.startPageNum = (pageNum -1) / pageBlockCount * pageBlockCount +1;

        int tempEndPageNum = this.startPageNum + pageBlockCount -1;
        this.endPageNum= Math.min(tempEndPageNum, this.totalPageCount);
    }
}
