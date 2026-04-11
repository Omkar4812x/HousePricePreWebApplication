package edu.omkar.services;

import java.util.List;
import edu.omkar.model.SearchHistoryModel;

public interface SearchHistoryService {
    boolean saveHistory(SearchHistoryModel model);
    List<SearchHistoryModel> getHistoryByUser(int userId);
}
