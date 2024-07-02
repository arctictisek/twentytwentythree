from utils import read_file_to_tuple


def run():
    tuple_str = read_file_to_tuple('inputs/day05Simple.txt')
    lines = split_to_tuple(tuple_str)
    print(lines)
    straight_lines = filter_out_non_straight(lines)
    print(straight_lines)


def split_to_tuple(tuple_string):
    return tuple(tuple(tuple(int(i) for i in part.split(',')) for part in line.split(' -> ')) for line in tuple_string)


def filter_out_non_straight(lines):
    return tuple(line for line in lines if line[0][0] == line[1][0] or line[0][1] == line[1][1])
