package edu.omkar.services;

import java.util.List;
import edu.omkar.model.SearchHistoryModel;
import edu.omkar.repository.SearchHistoryRepositoryImpl;

public class SearchHistoryServiceImpl implements SearchHistoryService {
    SearchHistoryRepositoryImpl repo = new SearchHistoryRepositoryImpl();

    @Override
    public boolean saveHistory(SearchHistoryModel model) { return repo.saveHistory(model); }

    @Override
    public List<SearchHistoryModel> getHistoryByUser(int userId) { return repo.getHistoryByUser(userId); }
}
