from utils import read_file_to_tuple


def run():
    tuple_str = read_file_to_tuple('inputs/day04.txt')
    tuple_of_integers = tuple(int(num) for num in tuple_str[0].split(','))
    # print(tuple_of_integers)
    tables = tables_strings_to_tuple(split_into_tuples_and_drop_last(tuple_str[2:]))
    # print(tables)
    result = find_last_winner_and_compute_result(tables, tuple_of_integers)
    print(result)
    # print(tables)


def split_into_tuples_and_drop_last(original_tuple, size=6):
    return tuple(original_tuple[i:i+size-1] for i in range(0, len(original_tuple), size))


def string_to_list_of_ints(s):
    return list([int(item), False] for item in s.split())


def table_string_to_tuple(table):
    return list(string_to_list_of_ints(line) for line in table)


def tables_strings_to_tuple(tables):
    return list(table_string_to_tuple(table) for table in tables)


def mark_number_in_table(table, number):
    for line in table:
        for inner_list in line:
            if inner_list[0] == number:
                inner_list[1] = True


def horizontal_winner(table):
    return any(all(item[1] for item in line) for line in table)


def vertical_winner(table):
    return horizontal_winner(zip(*table))


def any_winner(table):
    return horizontal_winner(table) or vertical_winner(table)


def find_first_winner_and_compute_result(tables, numbers):
    for i in numbers:
        for table in tables:
            mark_number_in_table(table, i)
            if any_winner(table):
                return sum(element[0] for row in table for element in row if not element[1]) * i


def find_last_winner_and_compute_result(tables, numbers):
    winners = 0
    no_of_tables = len(tables)
    for i in numbers:
        for table in tables:
            mark_number_in_table(table, i)
        for table in tables:
            if any_winner(table):
                tables.remove(table)
                winners += 1
                if winners == no_of_tables:
                    return sum(element[0] for row in table for element in row if not element[1]) * i


