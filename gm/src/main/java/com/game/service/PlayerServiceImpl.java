package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PlayerServiceImpl implements PlayerService{

    private PlayerRepository playerRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");


    @Autowired
    public void setPlayerRepository(PlayerRepository repository) {
        playerRepository = repository;
    }

    @Override
    public Player create(Player player) {
        /*final long newId = PLAYER_ID_HOLDER.incrementAndGet();
        player.setId(newId);
        PLAYERS_MAP.put(newId, player);*/
        return playerRepository.save(player);
    }

    @Override
    public List<Player> readAll(Specification<Player> specification, Pageable page) {
        return playerRepository.findAll(specification, page).getContent();
    }

    @Override
    public List<Player> readAll(Specification<Player> specification) {
        return playerRepository.findAll(specification);
    }

    @Override
    public List<Player> readAll() {
        return playerRepository.findAll();

        //return new ArrayList<Player>( PLAYERS_MAP.values());
    }

    @Override
    public Player getPlayer(Long id) {
        if (!playerRepository.existsById(id)) return null;
        return playerRepository.findById(id).get();
    }

    @Override
    public boolean update(Player player, Long id) {
        if (playerRepository.existsById(id)) {
            player.setId(id);
            playerRepository.save(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Specification<Player> filterName(String namePart) {
        return namePart == null ? null : (root, query, cb) -> {
            return cb.like(root.get("name"), "%" + namePart + "%");
        };
    }

    @Override
    public Specification<Player> filterRace(String raceName) {
        return raceName == null ? null : (root, query, cb) -> {
            return cb.equal(root.get("race"), Race.valueOf(raceName));
        };
    }

    @Override
    public Specification<Player> filterBanned(String banned) {
        return banned == null ? null : (root, query, cb) -> {
            return cb.equal(root.get("banned"), Boolean.valueOf(banned));
        };
    }

    @Override
    public Specification<Player> filterProfession(String professionName) {
        return professionName == null ? null : (root, query, cb) -> {
            return cb.equal(root.get("profession"), Profession.valueOf(professionName));
        };
    }

    @Override
    public Specification<Player> filterTitle(String titlePart) {
        return titlePart  == null ? null : (root, query, cb) -> {
            return cb.like(root.get("title"), "%" + titlePart + "%");
        };
    }

    @Override
    public Specification<Player> filterBirthdayFrom(String from) {
        return from == null ? null : (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("birthday"), new Date(Long.valueOf(from)));
        };
    }

    @Override
    public Specification<Player> filterBirthdayTo(String to) {
        return to == null ? null : (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("birthday"), new Date(Long.valueOf(to)));
        };
    }

    @Override
    public Specification<Player> filterExperienceFrom(String from) {
        return from == null ? null : (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("experience"), Long.valueOf(from));
        };
    }

    @Override
    public Specification<Player> filterExperienceTo(String to) {
        return to == null ? null : (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("experience"), Long.valueOf(to));
        };
    }

    @Override
    public Specification<Player> filterLevelFrom(String from) {
        return from == null ? null : (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("level"), Long.valueOf(from));
        };
    }

    @Override
    public Specification<Player> filterLevelTo(String to) {
        return to == null ? null : (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("level"), Long.valueOf(to));
        };
    }

    @Override
    public Specification<Player> filterBirthday(String from, String to) {
        return (root, query, cb) -> {
            Date fromDate;
            Date toDate;
            try {
                if (from!=null || from.length()!=0)
                    fromDate = new Date(Long.valueOf(from));
                else fromDate = null;
                if (to!=null || to.length()!=0)
                    toDate = new Date(Long.valueOf(to));
                else toDate = null;
            } catch (Exception e) {
                return null;
            }
            if (fromDate == null && toDate == null) return null;

            if (fromDate != null) {
                    return cb.greaterThanOrEqualTo(root.get("birthday"), fromDate);
            }
            if (toDate!=null) {
                return cb.lessThanOrEqualTo(root.get("birthday"), toDate);
            }
            return cb.between(root.get("birthday"), fromDate, toDate);
        };
    }


}
