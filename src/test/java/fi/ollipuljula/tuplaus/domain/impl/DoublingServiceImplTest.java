package fi.ollipuljula.tuplaus.domain.impl;

import fi.ollipuljula.tuplaus.domain.exception.NoBalanceException;
import fi.ollipuljula.tuplaus.db.AccountDao;
import fi.ollipuljula.tuplaus.db.DoublingChoice;
import fi.ollipuljula.tuplaus.db.GameTransactionDao;
import fi.ollipuljula.tuplaus.db.UserDao;
import fi.ollipuljula.tuplaus.db.entity.Account;
import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import fi.ollipuljula.tuplaus.db.entity.User;
import fi.ollipuljula.tuplaus.rest.model.DoubleDto;
import fi.ollipuljula.tuplaus.rest.model.PlayDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@SpringBootTest
class DoublingServiceImplTest {

    final static Logger log = LoggerFactory.getLogger(DoublingServiceImplTest.class);

    @InjectMocks
    @Spy
    private DoublingServiceImpl doublingService;
    @Mock
    private UserDao userDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private GameTransactionDao gameTransactionDao;

    @Test
    void testPlayOnceAndLose() {
        // given
        Account account = new Account();
        User user = new User();

        // when
        GameTransaction result = this.play(false, 10, 5000, account, user);

        // then
        assertEquals(4990, account.getBalance(), "balance mismatch");
        assertEquals(GameTransaction.Status.SETTLED, result.getStatus(), "lost game should settle the transaction");
        verify(userDao).find(user.getId());
        verify(accountDao).update(user.getAccount());
        verify(gameTransactionDao).create(result);
    }

    @Test
    void testPlayOnceWinAndCashout() {
        // given
        Account account = new Account();
        User user = new User();
        GameTransaction result = this.play(true, 10, 5000, account, user);
        final GameTransaction.Status status = result.getStatus();
        when(gameTransactionDao.find(result.getId())).thenReturn(result);

        // when
        DoubleDto doubleDto = new DoubleDto(false, result.getId(), null);
        GameTransaction gameTransaction = doublingService.playDouble(doubleDto);

        // then
        assertEquals(5010, account.getBalance(), "balance mismatch");
        assertEquals(GameTransaction.Status.OPEN, status, "settled game can't be continued");
        assertEquals(GameTransaction.Status.SETTLED, gameTransaction.getStatus(), "discontinued game should settle the transaction");
        verify(gameTransactionDao).find(doubleDto.getGameTransactionId());
        verify(accountDao, times(2)).update(account);
        verify(gameTransactionDao).update(gameTransaction);
    }

    @Test
    void testAccountHasNoBalance() {
        User user = new User();
        assertThrowsExactly(NoBalanceException.class,
                () -> this.play(true, 1000, 50, new Account(), user));
        verify(userDao).find(user.getId());
        verifyNoMoreInteractions(userDao, accountDao, gameTransactionDao);
    }

    @Test
    void testRollDice() {
        @AllArgsConstructor
        @Getter
        @ToString
        class RollDiceVariation {
            private int bet;
            private DoublingChoice doublingChoice;
            private int drawn;
        }

        List<RollDiceVariation> inputs = new ArrayList<>();
        inputs.add(new RollDiceVariation(10, DoublingChoice.SMALL, 6));
        inputs.add(new RollDiceVariation(10, DoublingChoice.SMALL, 7));
        inputs.add(new RollDiceVariation(10, DoublingChoice.SMALL, 8));
        inputs.add(new RollDiceVariation(10, DoublingChoice.BIG, 6));
        inputs.add(new RollDiceVariation(10, DoublingChoice.BIG, 7));
        inputs.add(new RollDiceVariation(10, DoublingChoice.BIG, 8));

        for (RollDiceVariation input : inputs) {
            when(doublingService.drawNumber(anyInt(), anyInt())).thenReturn(input.getDrawn());
            DoublingServiceImpl.GameResult gameResult = doublingService.play(input.getDoublingChoice(), input.getBet());
            assertEquals(((input.getDrawn() < 7 && input.getDoublingChoice().equals(DoublingChoice.SMALL))
                    || input.getDrawn() > 7 && input.getDoublingChoice().equals(DoublingChoice.BIG)), gameResult.isWin());
            assertEquals(input.getDrawn(), gameResult.getDrawn());
            if(gameResult.isWin()) {
                assertEquals(input.getBet() * 2, gameResult.getWinnings());
            } else {
                assertEquals(0, gameResult.getWinnings());
            }

            log.info(String.format("Given '%s' and game results '%s'", input, gameResult));
        }
    }

    GameTransaction play(boolean win, double bet, double balance, Account account, User user) {
        // given
        final double winnings = 2 * bet * (win ? 1 : 0);
        final int drawn = win ? 13 : 6;
        user.setLastname("John");
        user.setLastname("Doe");
        user.setId(1L);
        user.setAccount(account);
        account.setBalance(balance);
        account.setUser(user);
        user.setAccount(account);
        PlayDto dto = new PlayDto(user.getId(), bet, win ? DoublingChoice.BIG : DoublingChoice.SMALL);
        DoublingServiceImpl.GameResult gameResult = new DoublingServiceImpl.GameResult(win, winnings, drawn);

        when(userDao.find(dto.getUserId())).thenReturn(user);
        doReturn(gameResult).when(doublingService).play(any(), anyDouble());

        // when
        GameTransaction gameTransaction = doublingService.play(dto);
        gameTransaction.setId(1L);
        return gameTransaction;
    }
}