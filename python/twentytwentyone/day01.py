from utils import read_file_to_tuple


def run():
    tuple_str = read_file_to_tuple('inputs/day01.txt')
    int_tuple = tuple(int(x) for x in tuple_str)
    for generator in [generate_pairs_with_gap, lambda t: generate_pairs_with_gap(t, 3)]:
        result = count_larger(int_tuple, generator)
        print(result)


def generate_pairs_with_gap(int_tuple, gap=1):
    return zip(int_tuple, int_tuple[gap:])


def count_larger(int_tuple, generator):
    return sum(1 for current, nextValue in generator(int_tuple) if nextValue > current)
