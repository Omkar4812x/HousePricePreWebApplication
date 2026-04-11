package edu.omkar.repository;

import java.util.List;
import edu.omkar.model.SearchHistoryModel;

public interface SearchHistoryRepository {
    boolean saveHistory(SearchHistoryModel model);
    List<SearchHistoryModel> getHistoryByUser(int userId);
}
