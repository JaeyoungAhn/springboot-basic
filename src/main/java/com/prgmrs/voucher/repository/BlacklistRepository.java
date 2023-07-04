package com.prgmrs.voucher.repository;

import com.prgmrs.voucher.database.FileBlacklistDatabase;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class BlacklistRepository {
    private static final String FILE_PATH = "csv/blacklist.csv";
    private final FileBlacklistDatabase fileBlacklistDatabase;

    public BlacklistRepository(FileBlacklistDatabase fileBlacklistDatabase) {
        this.fileBlacklistDatabase = fileBlacklistDatabase;
    }

    public Map<UUID, String> findAll() {
        return fileBlacklistDatabase.load(FILE_PATH);
    }
}
