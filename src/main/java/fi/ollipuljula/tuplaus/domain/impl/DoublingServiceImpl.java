package fi.ollipuljula.tuplaus.domain.impl;

import fi.ollipuljula.tuplaus.domain.DoublingService;
import fi.ollipuljula.tuplaus.domain.exception.IncorrectGameEvent;
import fi.ollipuljula.tuplaus.domain.exception.NoBalanceException;
import fi.ollipuljula.tuplaus.db.DoublingChoice;
import fi.ollipuljula.tuplaus.db.AccountDao;
import fi.ollipuljula.tuplaus.db.GameTransactionDao;
import fi.ollipuljula.tuplaus.db.UserDao;
import fi.ollipuljula.tuplaus.db.entity.Account;
import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import fi.ollipuljula.tuplaus.db.entity.User;
import fi.ollipuljula.tuplaus.rest.model.DoubleDto;
import fi.ollipuljula.tuplaus.rest.model.PlayDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class DoublingServiceImpl implements DoublingService {
    private final GameTransactionDao gameTransactionDao;
    private final UserDao userDao;
    private final AccountDao accountDao;

    @Autowired
    public DoublingServiceImpl(GameTransactionDao gameTransactionDao, UserDao userDao, AccountDao accountDao) {
        this.gameTransactionDao = gameTransactionDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @Override
    @Transactional
    public GameTransaction play(PlayDto dto) {
        User user = userDao.find(dto.getUserId());
        Account account = user.getAccount();

        if(account.getBalance() < dto.getBet()) {
            throw new NoBalanceException();
        }

        account.setBalance(account.getBalance() - dto.getBet());
        accountDao.update(account);

        GameResult gameResult = this.play(dto.getChoice(), dto.getBet());

        GameTransaction gameTransaction = new GameTransaction();
        gameTransaction.setBet(dto.getBet());
        gameTransaction.setUser(user);
        gameTransaction.setChoice(dto.getChoice());
        gameTransaction.setDrawn(gameResult.getDrawn());
        gameTransaction.setWinnings(gameResult.getWinnings());
        gameTransaction.setExecutionTime(LocalDateTime.now());
        gameTransaction.setStatus(gameResult.isWin() ? GameTransaction.Status.OPEN : GameTransaction.Status.SETTLED);
        gameTransactionDao.create(gameTransaction);

        return gameTransaction;
    }

    @Transactional
    public GameTransaction playDouble(DoubleDto dto) {
        GameTransaction gameTransaction = gameTransactionDao.find(dto.getGameTransactionId());
        if(!GameTransaction.Status.OPEN.equals(gameTransaction.getStatus())) {
            throw new IncorrectGameEvent();
        }
        User user = gameTransaction.getUser();
        Account account = user.getAccount();

        if(!dto.isPlay()) {
            account.setBalance(account.getBalance() + gameTransaction.getWinnings());
            accountDao.update(account);

            gameTransaction.setStatus(GameTransaction.Status.SETTLED);
            gameTransactionDao.update(gameTransaction);

            return gameTransaction;
        } else {
            GameResult gameResult = this.play(dto.getChoice(), gameTransaction.getWinnings());

            GameTransaction newGameTransaction = new GameTransaction();
            newGameTransaction.setParent(gameTransaction);
            newGameTransaction.setBet(gameTransaction.getWinnings());
            newGameTransaction.setUser(user);
            newGameTransaction.setChoice(dto.getChoice());
            newGameTransaction.setDrawn(gameResult.getDrawn());
            newGameTransaction.setWinnings(gameResult.getWinnings());
            newGameTransaction.setExecutionTime(LocalDateTime.now());
            newGameTransaction.setStatus(gameResult.isWin() ? GameTransaction.Status.OPEN : GameTransaction.Status.SETTLED);

            gameTransaction.setStatus(GameTransaction.Status.SETTLED);
            gameTransaction.setChild(newGameTransaction);

            gameTransactionDao.update(gameTransaction);
            gameTransactionDao.create(newGameTransaction);

            return newGameTransaction;
        }
    }

    protected GameResult play(DoublingChoice choice, double bet) {
        final int min = 1;
        final int max = 13;
        final double multiplier = 2;
        int num = drawNumber(min, max);
        if((num < 7 && choice.equals(DoublingChoice.SMALL))
                || num > 7 && choice.equals(DoublingChoice.BIG)) {
            return new GameResult(true, multiplier * bet, num);
        } else {
            return new GameResult(false, 0, num);
        }
    }

    protected int drawNumber(int min, int max) {
        return new Random().nextInt(min, max + 1);
    }

    @Getter
    @AllArgsConstructor
    @ToString
    protected static class GameResult {
        private boolean win;
        private double winnings;
        private int drawn;
    }
}
