package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.security.SecurityPermission;
import java.util.List;

public interface PlayerService {
    Player create(Player player);

    List<Player> readAll(Specification<Player> specification,  Pageable page);

    List<Player> readAll(Specification<Player> specification);

    List<Player> readAll();

    Player getPlayer(Long id);

    boolean update(Player player, Long id);

    boolean delete(Long id);

    Specification<Player> filterName(String namePart);

    Specification<Player> filterTitle(String titlePart);

    Specification<Player> filterBirthday(String from, String to);

    Specification<Player> filterBirthdayFrom(String from);

    Specification<Player> filterBirthdayTo(String to);

    Specification<Player> filterExperienceFrom(String from);

    Specification<Player> filterExperienceTo(String to);

    Specification<Player> filterLevelFrom(String from);

    Specification<Player> filterLevelTo(String to);

    Specification<Player> filterRace(String raceName);

    Specification<Player> filterProfession(String professionName);

    Specification<Player> filterBanned(String banned);

}
