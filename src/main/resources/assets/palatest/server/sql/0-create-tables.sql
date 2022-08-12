CREATE TABLE `grinderCrafts`
(
    `id`          INT          NOT NULL,
    `playerName`  VARCHAR(255) NOT NULL,
    `craftedItem` VARCHAR(255) NOT NULL,
    `craftDate`   DATE         NOT NULL,
    `worldName`   VARCHAR(255) NOT NULL,
    `x`           FLOAT(24)    NOT NULL,
    `y`           FLOAT(24)    NOT NULL,
    `z`           FLOAT(24)    NOT NULL
);

ALTER TABLE `grinderCrafts`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `grinderCrafts`
    MODIFY `id` int(255) NOT NULL AUTO_INCREMENT;
