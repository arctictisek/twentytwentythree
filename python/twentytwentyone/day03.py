from utils import read_file_to_tuple


def run():
    tuple_str = read_file_to_tuple('inputs/day03.txt')
    # zeros = count_zeros(tuple_str)
    # print(zeros)
    # binary_result = build_new_tuple(zeros, len(tuple_str))
    # print(binary_result[0] * binary_result[1])
    o2_rating = oxygen_rating(tuple_str)
    ceeOtwoRating = co2_rating(tuple_str)
    print(o2_rating * ceeOtwoRating)


def count_zeros(strings):
    return tuple(sum(char == '0' for char in position) for position in zip(*strings))


def build_new_tuple(zero_count, no_of_lines):
    new_tuple = tuple('0' if count > (no_of_lines - count) else '1' for count in zero_count)
    inverted_tuple = tuple('1' if char == '0' else '0' for char in new_tuple)
    return int(''.join(new_tuple), 2), int(''.join(inverted_tuple), 2)


def find_oxygen_criteria(strings, cur_position):
    zero_in_pos = count_zeros(strings)[cur_position]
    if zero_in_pos > len(strings) - zero_in_pos:
        return '0'
    else:
        return '1'


def find_co2_criteria(strings, cur_position):
    if find_oxygen_criteria(strings, cur_position) == '0':
        return '1'
    else:
        return '0'


def oxygen_rating_for_pos(strings, cur_position):
    criteria = find_oxygen_criteria(strings, cur_position)
    return tuple(s for s in strings if s[cur_position] == criteria)


def co2_rating_for_pos(strings, cur_position):
    criteria = find_co2_criteria(strings, cur_position)
    return tuple(s for s in strings if s[cur_position] == criteria)


def oxygen_rating(strings, cur_position=-1):
    if len(strings) == 1:
        return int(strings[0], 2)
    else:
        return oxygen_rating(oxygen_rating_for_pos(strings, 1 + cur_position), 1 + cur_position)


def co2_rating(strings, cur_position=-1):
    if len(strings) == 1:
        return int(strings[0], 2)
    else:
        return co2_rating(co2_rating_for_pos(strings, 1 + cur_position), 1 + cur_position)
