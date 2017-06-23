package com.shangshow.showlive.network.service.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lorntao on 16-8-6.
 */
public class LabelModel implements Serializable {
    int pageNum;
    int pageSize;
    int size;
    int startRow;
    int endRow;
    int total;
    int pages;
    List<LabelInfo> list;
    int firstPage;
    int prePage;
    int nextPage;
    int lastPage;
    boolean isFirstPage;
    boolean isLastPage;
    boolean hasPreviousPage;
    boolean hasNextPage;
    int navigatePages;

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  public int getEndRow() {
    return endRow;
  }

  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public List<LabelInfo> getList() {
    return list;
  }

  public void setList(List<LabelInfo> list) {
    this.list = list;
  }

  public int getFirstPage() {
    return firstPage;
  }

  public void setFirstPage(int firstPage) {
    this.firstPage = firstPage;
  }

  public int getPrePage() {
    return prePage;
  }

  public void setPrePage(int prePage) {
    this.prePage = prePage;
  }

  public int getNextPage() {
    return nextPage;
  }

  public void setNextPage(int nextPage) {
    this.nextPage = nextPage;
  }

  public int getLastPage() {
    return lastPage;
  }

  public void setLastPage(int lastPage) {
    this.lastPage = lastPage;
  }

  public boolean isFirstPage() {
    return isFirstPage;
  }

  public void setFirstPage(boolean firstPage) {
    isFirstPage = firstPage;
  }

  public boolean isLastPage() {
    return isLastPage;
  }

  public void setLastPage(boolean lastPage) {
    isLastPage = lastPage;
  }

  public boolean isHasPreviousPage() {
    return hasPreviousPage;
  }

  public void setHasPreviousPage(boolean hasPreviousPage) {
    this.hasPreviousPage = hasPreviousPage;
  }

  public boolean isHasNextPage() {
    return hasNextPage;
  }

  public void setHasNextPage(boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }

  public int getNavigatePages() {
    return navigatePages;
  }

  public void setNavigatePages(int navigatePages) {
    this.navigatePages = navigatePages;
  }

  public int getPageNum() {

    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }
}
