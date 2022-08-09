package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.api.controller.exception.NotFoundException;
import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.repository.GroupSpendingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GroupSpendingServiceTests {
    @Mock
    private GroupSpendingRepository groupRepository;

    @InjectMocks
    private GroupSpendingService groupService;

    private GroupSpending groupSpending;

    private GroupSpending newGroupSpending;

    @BeforeEach
    public void setup() {
        groupSpending = new GroupSpending();
        groupSpending.setAdditionals(BigDecimal.valueOf(20));
        groupSpending.setDiscounts(BigDecimal.valueOf(27));
        groupSpending.setGlobalBill(BigDecimal.valueOf(69));
        groupSpending.setHasWaiterAdd(false);


        newGroupSpending = new GroupSpending();
        newGroupSpending.setAdditionals(BigDecimal.valueOf(40));
        newGroupSpending.setDiscounts(BigDecimal.valueOf(23));
        newGroupSpending.setGlobalBill(BigDecimal.valueOf(99));
        newGroupSpending.setHasWaiterAdd(true);
    }

    // Teste JUnit para método GroupSpendingService.create(GroupSpending)
    @DisplayName("Teste JUnit: GroupSpendingService.create(GroupSpending)")
    @Test
    public void givenObjectGroupSpending_whenCreate_thenReturnSavedGroupSpending() {

        // DADO: pré-condição ou setup
        // do GroupSpendingService, vemos que utilizamos o GroupSpendingRepository.save, que precisa de stubbing.
        given(groupRepository.save(groupSpending)).willReturn(groupSpending);

        // QUANDO: ação ou comportamento a ser testado
        GroupSpending savedGroupSpending = groupService.create(groupSpending);

        // ENTÃO: verificação das saídas
        System.out.println(savedGroupSpending);
        assertThat(savedGroupSpending).isNotNull();
    }

    // Teste JUnit para método GroupSpendingService.findById(Long) - cenário positivo
    @DisplayName("Teste JUnit: GroupSpendingService.findById(Long) - cenário positivo")
    @Test
    public void givenGroupSpendingId_whenFindById_thenReturnGroupSpending() {

        // DADO: pré-condição ou setup
        given(groupRepository.findById(groupSpending.getId())).willReturn(Optional.of(groupSpending));

        // QUANDO: ação ou comportamento a ser testado
        GroupSpending savedGroupSpending = groupService.findById(groupSpending.getId());

        // ENTÃO: verificação das saídas
        assertThat(savedGroupSpending).isNotNull();
        System.out.println(savedGroupSpending);

    }

    // Teste JUnit para método GroupSpendingService.findById(Long) - cenário negativo
    @DisplayName("Teste JUnit: GroupSpendingService.findById(Long) - cenário negativo")
    @Test
    public void givenGroupSpendingId_whenFindById_thenReturnExcecao() {

        NotFoundException notFoundException = new NotFoundException("Não encontrado");

        // DADO: pré-condição ou setup
        given(groupRepository.findById(groupSpending.getId())).willThrow(notFoundException);

        // QUANDO e ENTÃO: ação ou comportamento a ser testado e verificação das saídas
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
            groupService.findById(groupSpending.getId());
        });

    }

    // Teste JUnit para groupService.findAll
    @DisplayName("Teste JUnit: GroupSpendingService.findAll(PageRequest)")
    @Test
    public void givenPeopleList_whenFindAll_thenReturnPageOfPeople() {

        // DADO: pré-condição ou setup
        List<GroupSpending> peopleList = new ArrayList<>();
        peopleList.add(groupSpending);
        peopleList.add(newGroupSpending);
        Page<GroupSpending> page = new PageImpl<>(peopleList);
        PageRequest pageRequest = PageRequest.of(1, 20);

        // stubbing
        given(groupRepository.findAll(pageRequest)).willReturn(page);


        // QUANDO: ação ou comportamento a ser testado
        Page<GroupSpending> response = groupService.findAll(pageRequest);


        // ENTÃO: verificação das saídas
        System.out.println(response.toString());
        assertThat(response).isNotNull();
        response.getContent().forEach(x -> {
            assertThat(x).isOfAnyClassIn(GroupSpending.class);
            System.out.println(x.toString());
        });

    }

    // Teste JUnit: GroupSpendingService.update(Long, GroupSpending)
    @DisplayName("Teste JUnit: GroupSpendingService.update(Long, GroupSpending)")
    @Test
    public void givenObjectGroupSpending_whenUpdateGroupSpending_thenReturnUpdatedGroupSpending() {

        // DADO: pré-condição ou setup
        given(groupRepository.save(groupSpending)).willReturn(groupSpending);

        given(groupRepository.findById(groupSpending.getId())).willReturn(Optional.of(groupSpending));

        // atualizando objeto:
        groupSpending.setDiscounts(BigDecimal.valueOf(203));


        // QUANDO: ação ou comportamento a ser testado
        groupService.update(groupSpending.getId(), groupSpending);

        // ENTÃO: verificação das saídas
        // conferindo se groupRepository.save foi chamado apenas uma vez
        verify(groupRepository, times(1)).save(groupSpending);
        verify(groupRepository, times(1)).findById(groupSpending.getId());
        assertThat(groupSpending.getDiscounts()).isEqualTo(BigDecimal.valueOf(203));

    }

    // Teste JUnit: GroupSpendingService.delete(Long)
    @DisplayName("Teste JUnit: GroupSpendingService.delete(Long)")
    @Test
    public void givenObjectGroupSpending_whenDelete_thenReturnNoContent() {

        // DADO: pré-condição ou setup
        //build do setup
        willDoNothing().given(groupRepository).deleteById(groupSpending.getId());

        // QUANDO: ação ou comportamento a ser testado
        groupService.delete(groupSpending.getId());

        // ENTÃO: verificação das saídas
        verify(groupRepository, times(1)).deleteById(groupSpending.getId());

    }
}
